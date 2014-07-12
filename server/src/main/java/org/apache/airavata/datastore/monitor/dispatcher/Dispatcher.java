package org.apache.airavata.datastore.monitor.dispatcher;

import org.apache.airavata.datastore.monitor.FileUpdateMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class);
    private DispatchQueue queue;

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
                FileUpdateMessage directoryUpdateMessage = null;
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
    private void dispatch(FileUpdateMessage directoryUpdateMessage) throws Exception {
        logger.info("Dispatching new message for file: " + directoryUpdateMessage.getFileName());
    }

}
