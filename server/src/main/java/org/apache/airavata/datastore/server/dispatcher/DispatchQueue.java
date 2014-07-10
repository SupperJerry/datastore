package org.apache.airavata.datastore.server.dispatcher;

import org.apache.airavata.datastore.server.monitor.DirectoryUpdateMessage;

import java.util.LinkedList;

public class DispatchQueue {
    private LinkedList<DirectoryUpdateMessage> queue;
    public static DispatchQueue instance;

    private DispatchQueue() {
        queue = new LinkedList<DirectoryUpdateMessage>();
    }

    /**
     * Put a message into the dispatch queue
     *
     * @param directoryUpdateMessage
     */
    public synchronized void addMsgToQueue(DirectoryUpdateMessage directoryUpdateMessage){
        queue.add(directoryUpdateMessage);
        notifyAll();
    }

    /**
     * Returns a DirectoryUpdateMessage object from the dispatcher queue
     *
     * @return DirectoryUpdateMessage object
     */
    public synchronized DirectoryUpdateMessage getMsgFromQueue(){
        DirectoryUpdateMessage directoryUpdateMessage = queue.pollFirst();
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
    public static DispatchQueue getInstance(){
        if(instance == null ){
            instance = new DispatchQueue();
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
