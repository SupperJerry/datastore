package org.apache.airavata.datastore.monitor;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.monitor.dispatcher.Dispatcher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

public class MonitorService {
    private static final Logger logger = LogManager.getLogger(MonitorService.class);

    @Autowired
    private IMonitor iMonitor;

    @Autowired
    private Dispatcher dispatcher;


    public void startService() throws Exception {
        //Loading Log4J property from External Source
        if (new File(Properties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(Properties.LOG4J_PROPERTY_FILE);
        }

        System.out.println("\nStarting Monitor Server...!\n");

        //Starting directory org.apache.airavata.datastore.monitor
        Properties properties = Properties.getInstance();
        iMonitor.startMonitor(Paths.get(properties.getDataRoot()));

        //Starting directory update message dispatcher
        dispatcher.startDispatcher();
    }

    public void stopService(){
        iMonitor.stopMonitor();
        System.out.println("\nGood bye from Monitor Server...!\n");
    }
}
