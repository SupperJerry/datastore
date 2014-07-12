package org.apache.airavata.datastore.monitor.dispatcher;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.airavata.datastore.parser.impl.AutoDetectParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class);
    private DispatchQueue queue;
    private IParser parser;

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
        parser = new AutoDetectParser();
    }

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                FileWatcherMessage directoryUpdateMessage = null;
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
     * @param fileUpdateMessage
     * @throws Exception
     */
    private void dispatch(FileWatcherMessage fileUpdateMessage) throws Exception {
        logger.info("Dispatching new message for file: " + fileUpdateMessage.getFileName());
        HashMap<String, String> metadata = parser.parse(fileUpdateMessage);
    }

}
