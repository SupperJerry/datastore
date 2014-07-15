package org.apache.airavata.datastore;

import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.monitor.MonitorService;
import org.apache.airavata.datastore.orchestrator.OrchestratorService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class DataStore {

    private static final Logger logger = LogManager.getLogger(MonitorService.class);

    public static void main(String[] args) throws Exception {
        //Loading Log4J property from External Source
        if (new File(Properties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(Properties.LOG4J_PROPERTY_FILE);
        }


        BeanFactory context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
        final MonitorService monitorService = (MonitorService) context.getBean("monitorService");
        monitorService.startService();

        final OrchestratorService orchestratorService = (OrchestratorService) context.getBean("orchestratorService");
        orchestratorService.startService();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("ShutDown called...");
                monitorService.stopService();
                orchestratorService.stopService();
            }
        });
    }
}
