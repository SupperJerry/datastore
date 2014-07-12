package org.apache.airavata.datastore.monitor.dispatcher;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.Parser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class);
    @Autowired
    private DispatchQueue dispatchQueue;
    @Autowired
    private IParserResolver iParserResolver;

    private ExecutorService exec;


    public Dispatcher(){
        init();
    }

    private void init(){
        try{
            exec = Executors.newFixedThreadPool(Properties.getInstance().getMaxParserThreads());
        }catch (Exception e){
            logger.error(e.toString());
            exec = Executors.newFixedThreadPool(100);
        }
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
        Parser parser = iParserResolver.getParser(fileUpdateMessage);
        exec.execute(parser);
        logger.info("Dispatched new message for file: " + fileUpdateMessage.getFileName());
    }

}
