package org.apache.airavata.datastore.monitor;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.monitor.dispatcher.Dispatcher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class MonitorService {
    private static final Logger logger = LogManager.getLogger(MonitorService.class);
    private IMonitor monitor;
    private Dispatcher dispatcher;


    public MonitorService() throws Exception {
        dispatcher =  new Dispatcher();
    }

    @Autowired
    public void setMonitor(IMonitor monitorImpl) throws Exception{
        monitor = monitorImpl;
    }

    public void startService() throws Exception {
        //Loading Log4J property from External Source
        if (new File(Properties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(Properties.LOG4J_PROPERTY_FILE);
        }

        System.out.println("\nStarting Monitor Server...!\n");

        //Starting directory org.apache.airavata.datastore.monitor
        Properties properties = Properties.getInstance();
        monitor.startMonitor(Paths.get(properties.getDataRoot()));

        //Starting directory update message dispatcher
        dispatcher.startDispatcher();
    }

    public void stopService(){
        monitor.stopMonitor();
        System.out.println("\nGood bye from Monitor Server...!\n");
    }
}
