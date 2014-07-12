package org.apache.airavata.datastore.monitor.dispatcher;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class);
    @Autowired
    private DispatchQueue dispatchQueue;
    @Autowired
    private IParser iParser;

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                FileWatcherMessage directoryUpdateMessage = null;
                while (true) {
                    directoryUpdateMessage = dispatchQueue.getMsgFromQueue();
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
        HashMap<String, String> metadata = iParser.parse(fileUpdateMessage);
        if(metadata!=null){
            //@Todo
            //Code for indexing the metadata
        }
    }

}
