package org.apache.airavata.datastore.orchestrator;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;
import org.apache.airavata.datastore.orchestrator.dispatcher.MetadataDispatcherQueue;
import org.apache.airavata.datastore.orchestrator.dispatcher.MonitorDispatcherQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class OrchestratorService {
    private static final Logger logger = LogManager.getLogger(OrchestratorService.class);

    @Autowired
    private MetadataDispatcherQueue metadataDispatcherQueue;

    @Autowired
    private MonitorDispatcherQueue monitorDispatcherQueue;

    private static OrchestratorService instance;

    private OrchestratorService(){
        OrchestratorService.instance = this;
    }

    public static OrchestratorService getInstance(){
        if(OrchestratorService.instance==null){
            return new OrchestratorService();
        }

        return OrchestratorService.instance;
    }

    public void startService() throws Exception {
        //Loading Log4J property from External Source
        if (new File(Properties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(Properties.LOG4J_PROPERTY_FILE);
        }

        System.out.println("\nStarting Orchestrator Server...!\n");

    }

    public void stopService(){
        System.out.println("\nGood bye from Orchestrator Server...!\n");
    }

    public void addNewFileMonitorMessage(FileMonitorMessage fileMonitorMessage){
        monitorDispatcherQueue.addMsgToQueue(fileMonitorMessage);
    }

    public void addNewFileMetadata(FileMetadata fileMetadata){
        metadataDispatcherQueue.addMetadataToQueue(fileMetadata);
    }

}
