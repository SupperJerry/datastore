package org.apache.airavata.datastore.orchestrator.dispatcher;

import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MetadataDispatcher {

    private final Logger logger = LogManager.getLogger(MetadataDispatcher.class);

    @Autowired
    private MetadataDispatcherQueue metadataDispatcherQueue;

    /**
     * Starts the message dispatcher
     */
    public void startDispatcher(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                FileMetadata fileMetadata = null;
                while (true) {
                    fileMetadata = metadataDispatcherQueue.getMetadataFromQueue();
                    if (fileMetadata != null) {
                        try {
                            dispatch(fileMetadata);
                        } catch (Exception e) {
                            logger.error(e.toString());
                        }
                    }
                }
            }
        })).start();
    }

    /**
     * Dispatch single file metadata
     *
     * @param fileMetadata
     * @throws Exception
     */
    private void dispatch(FileMetadata fileMetadata){
        //@Todo
        //Write to database
    }

}
