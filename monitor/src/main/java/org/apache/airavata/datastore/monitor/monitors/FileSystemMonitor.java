package org.apache.airavata.datastore.monitor.monitors;

import org.apache.airavata.datastore.monitor.DirectoryUpdateMessage;
import org.apache.airavata.datastore.monitor.IMonitor;
import org.apache.airavata.datastore.monitor.dispatcher.DispatchQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class FileSystemMonitor implements IMonitor {

    private final Logger logger = LogManager.getLogger(FileSystemMonitor.class);

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private boolean runMonitor = false;
    private boolean trace = false;

    private DispatchQueue dispatchQueue;

    public FileSystemMonitor() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();

        //Initialize dispatch queue
        this.dispatchQueue = DispatchQueue.getInstance();
    }

    /**
     * Start directory monitoring
     */
    @Override
    public void startMonitor(String path) throws IOException {
        init(path);
        runMonitor = true;
        processEvents();
        logger.info("Started directory watching");
    }

    /**
     * Stop directory monitoring
     */
    @Override
    public void stopMonitor() {
        runMonitor = false;
        logger.info("Stopped directory watching");
    }

    /**
     * Initializing the FileSystemMonitor
     *
     * @param path
     * @throws IOException
     */
    private void init(String path) throws IOException {
        this.trace = false;

        Path dir = Paths.get(path);
        logger.info("Scanning " + dir);
        registerAll(dir);
        logger.info("Done.");

        // enable trace after initial registration
        this.trace = true;

    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                logger.info("register: " + dir);
            } else {
                if (!dir.equals(prev)) {
                    logger.info("update: "+ prev + " -> "+ dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Processing directory update events
     */
    @SuppressWarnings("unchecked")
    private void processEvents(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while(runMonitor) {
                    WatchKey key;
                    try {
                        key = watcher.take();
                    } catch (InterruptedException x) {
                        logger.error(x.toString());
                        break;
                    }
                    for (WatchEvent<?> event: key.pollEvents()) {
                        WatchEvent.Kind kind = event.kind();
                        if (kind == OVERFLOW) {
                            continue;
                        }
                        WatchEvent<Path> ev = (WatchEvent<Path>)event;
                        Path fileName = ev.context();
                        DirectoryUpdateMessage directoryUpdateMessage;
                        if (kind == OVERFLOW) {
                            continue;
                        } else if (kind == ENTRY_CREATE) {
                            logger.info("New file created. File name: "+fileName.toString());
                            // file create event
                            directoryUpdateMessage = new DirectoryUpdateMessage(fileName.toString());
                            dispatchQueue.addMsgToQueue(directoryUpdateMessage);
                        } else if (kind == ENTRY_DELETE) {
                            // file delete event
                            logger.info("File deleted. File name: "+fileName.toString());
                        } else if (kind == ENTRY_MODIFY) {
                            // file modify event
                            logger.info("File modified. File name: "+fileName.toString());
                        };
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        logger.error("Directory watcher key is no longer valid");
                    }
                }
            }
        })).start();
    }
}