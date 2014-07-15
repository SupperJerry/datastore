package org.apache.airavata.datastore.orchestrator.dispatcher;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;
import org.apache.airavata.datastore.parser.Parser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MonitorDispatcher {

    private final Logger logger = LogManager.getLogger(MonitorDispatcher.class);

    @Autowired
    private MonitorDispatcherQueue monitorDispatcherQueue;

    @Autowired
    private MetadataDispatcherQueue metadataDispatcherQueue;

    @Autowired
    private IParserResolver iParserResolver;

    private ExecutorService exec;


    public MonitorDispatcher(){
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
                FileMonitorMessage directoryUpdateMessage = null;
                while (true) {
                    directoryUpdateMessage = monitorDispatcherQueue.getMsgFromQueue();
                    if (directoryUpdateMessage != null) {
                        try {
                            dispatch(directoryUpdateMessage);
                        } catch (Exception e) {
                            logger.error(e.toString());
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
    private void dispatch(final FileMonitorMessage fileUpdateMessage) throws Exception {
        logger.info("Dispatching new message for file: " + fileUpdateMessage.getFileName());
        exec.execute(new Runnable() {
            @Override
            public void run() {
                Parser parser = iParserResolver.getParser(fileUpdateMessage);
                FileMetadata fileMetadata = parser.parse();
                metadataDispatcherQueue.addMetadataToQueue(fileMetadata);
            }
        });
        logger.info("Dispatched new message for file: " + fileUpdateMessage.getFileName());
    }

}
