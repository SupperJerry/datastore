package org.apache.airavata.datastore.monitor;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.orchestrator.dispatcher.MonitorDispatcher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Paths;

public class MonitorService {
    private static final Logger logger = LogManager.getLogger(MonitorService.class);

    @Autowired
    private IMonitor iMonitor;

    @Autowired
    private MonitorDispatcher monitorDispatcher;


    public void startService() throws Exception {
        System.out.println("\nStarting Monitor Server...!\n");

        //Starting directory org.apache.airavata.datastore.monitor
        Properties properties = Properties.getInstance();
        iMonitor.startMonitor(Paths.get(properties.getDataRoot()));

        //Starting directory update message monitorDispatcher
        monitorDispatcher.startDispatcher();
    }

    public void stopService(){
        iMonitor.stopMonitor();
        System.out.println("\nGood bye from Monitor Server...!\n");
    }
}
