package org.apache.airavata.datastore.monitor.monitors;

import org.apache.airavata.datastore.monitor.Monitor;
import org.apache.airavata.datastore.monitor.DirectoryUpdateMessage;
import org.apache.airavata.datastore.monitor.dispatcher.DispatchQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileSystemMonitor implements Monitor {

    private final Logger logger = LogManager.getLogger(FileSystemMonitor.class);

    private final WatchService watcher;
    private final Path dir;
    private boolean runMonitor = false;
    private DispatchQueue dispatchQueue;

    public FileSystemMonitor(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        this.dir = dir;
        this.dispatchQueue = DispatchQueue.getInstance();
    }

    /**
     * Start directory monitoring
     */
    @Override
    public void startMonitor(){
        synchronized(this){
            runMonitor = true;
        }
        processEvents();
        logger.info("Started directory watching");
    }

    /**
     * Stop directory monitoring
     */
    @Override
    public void stopMonitor() {
        synchronized(this){
            runMonitor = false;
        }
        logger.info("Stopped directory watching");
    }

    /**
     * Processing directory update events
     */
    @Override
    @SuppressWarnings("unchecked")
    public void processEvents(){
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