package org.apache.airavata.datastore.orchestrator.dispatcher;

import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;

import java.util.LinkedList;

public class MetadataDispatcherQueue {
    private LinkedList<FileMetadata> queue;
    public static MetadataDispatcherQueue instance;

    private MetadataDispatcherQueue() {
        queue = new LinkedList<FileMetadata>();
    }

    /**
     * Put a message into the dispatch queue
     *
     * @param fileMetadata
     */
    public synchronized void addMetadataToQueue(FileMetadata fileMetadata){
        queue.add(fileMetadata);
        notifyAll();
    }

    /**
     * Returns a FileMetadata object from the dispatcher queue
     *
     * @return FileMetadata object
     */
    public synchronized FileMetadata getMetadataFromQueue(){
        FileMetadata fileMetadata = queue.pollFirst();
        while(fileMetadata ==null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fileMetadata = queue.pollFirst();
        }
        return fileMetadata;
    }

    /**
     * Returns a singleton dispatch queue object
     *
     * @return Dispatch queue object
     */
    public static MetadataDispatcherQueue getInstance(){
        if(instance == null ){
            instance = new MetadataDispatcherQueue();
        }
        return instance;
    }

    /**
     * Returns dispatch queue size
     *
     * @return queue size
     */
    public long getQueueSize(){
        return queue.size();
    }
}
