package org.apache.airavata.datastore.monitor.impl;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.monitor.IMonitor;
import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.monitor.dispatcher.DispatchQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class LocalFileSysMonitor implements IMonitor {

    private final Logger logger = LogManager.getLogger(LocalFileSysMonitor.class);

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private boolean runMonitor = false;
    private boolean trace = false;
    private Path rootDir = null;

    @Autowired
    private DispatchQueue dispatchQueue;

    public LocalFileSysMonitor() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
    }

    /**
     * Start directory monitoring
     */
    @Override
    public void startMonitor(Path path) throws IOException {
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
    private void init(Path path) throws IOException {
        this.rootDir = path;
        this.trace = false;

        logger.info("Scanning " + path);
        registerAll(path);
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
                        Path parentPath = rootDir.resolve(keys.get(key).toString());
                        FileWatcherMessage directoryUpdateMessage;

                        if (kind == OVERFLOW) {
                            continue;
                        } else if(kind == ENTRY_CREATE && Files.isDirectory(parentPath.resolve(fileName), NOFOLLOW_LINKS)){
                            try {
                                registerAll(parentPath.resolve(fileName));
                            } catch (IOException e) {
                                logger.error(e.toString());
                            }
                        } else if (kind == ENTRY_CREATE) {
                            // file create event
                            logger.info("New file created. File name: "+fileName.toString());
                            directoryUpdateMessage = new FileWatcherMessage(fileName.toString(),
                                    parentPath.toString()+ File.pathSeparator+fileName, Constants.FILE_CREATED);
                            dispatchQueue.addMsgToQueue(directoryUpdateMessage);
                        } else if (kind == ENTRY_DELETE) {
                            // file delete event
                            logger.info("File deleted. File name: "+fileName.toString());
                            directoryUpdateMessage = new FileWatcherMessage(fileName.toString(),
                                    parentPath.toString()+ File.pathSeparator+fileName, Constants.FILE_DELETED);
                            dispatchQueue.addMsgToQueue(directoryUpdateMessage);
                        } else if (kind == ENTRY_MODIFY) {
                            // file modify event
                            logger.info("File modified. File name: "+fileName.toString());
                            directoryUpdateMessage = new FileWatcherMessage(fileName.toString(),
                                    parentPath.toString()+ File.pathSeparator+fileName, Constants.FILE_MODIFIED);
                            dispatchQueue.addMsgToQueue(directoryUpdateMessage);
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