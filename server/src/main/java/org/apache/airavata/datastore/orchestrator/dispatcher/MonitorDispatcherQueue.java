package org.apache.airavata.datastore.orchestrator.dispatcher;

import org.apache.airavata.datastore.models.FileMonitorMessage;

import java.util.LinkedList;

public class MonitorDispatcherQueue {
    private LinkedList<FileMonitorMessage> queue;
    public static MonitorDispatcherQueue instance;

    private MonitorDispatcherQueue() {
        queue = new LinkedList<FileMonitorMessage>();
    }

    /**
     * Put a message into the dispatch queue
     *
     * @param directoryUpdateMessage
     */
    public synchronized void addMsgToQueue(FileMonitorMessage directoryUpdateMessage){
        queue.add(directoryUpdateMessage);
        notifyAll();
    }

    /**
     * Returns a DirectoryUpdateMessage object from the dispatcher queue
     *
     * @return DirectoryUpdateMessage object
     */
    public synchronized FileMonitorMessage getMsgFromQueue(){
        FileMonitorMessage directoryUpdateMessage = queue.pollFirst();
        while(directoryUpdateMessage ==null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            directoryUpdateMessage = queue.pollFirst();
        }
        return directoryUpdateMessage;
    }

    /**
     * Returns a singleton dispatch queue object
     *
     * @return Dispatch queue object
     */
    public static MonitorDispatcherQueue getInstance(){
        if(instance == null ){
            instance = new MonitorDispatcherQueue();
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
