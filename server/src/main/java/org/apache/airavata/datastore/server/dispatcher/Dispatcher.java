package org.apache.airavata.datastore.server.dispatcher;

import org.apache.airavata.datastore.server.monitor.DirectoryUpdateMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class);
    DispatchQueue queue;

    public Dispatcher() throws Exception {
        init();
    }

    /**
     * Dispatcher initializer
     *
     * @throws Exception
     */
    private void init() throws Exception {
        queue = DispatchQueue.getInstance();
    }

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                DirectoryUpdateMessage directoryUpdateMessage = null;
                while (true) {
                    directoryUpdateMessage = queue.getMsgFromQueue();
                    if (directoryUpdateMessage != null) {
                        try {
                            dispatch(directoryUpdateMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        })).start();
    }

    /**
     * Dispatch single message
     *
     * @param directoryUpdateMessage
     * @throws Exception
     */
    private void dispatch(DirectoryUpdateMessage directoryUpdateMessage) throws Exception {
        logger.info("Dispatching new message for file: " + directoryUpdateMessage.getFileName());
        System.out.println(directoryUpdateMessage.getFileName());
        String parserType = getParserType(directoryUpdateMessage);
    }

    /**
     * Method to identify the parser for the new file
     *
     * @param directoryUpdateMessage
     * @return
     * @throws Exception
     */
    private String getParserType(DirectoryUpdateMessage directoryUpdateMessage) throws Exception {
        String type = null;
        return type;
    }
}
