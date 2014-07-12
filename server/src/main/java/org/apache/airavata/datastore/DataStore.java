package org.apache.airavata.datastore;

import org.apache.airavata.datastore.monitor.MonitorService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DataStore {

    private static final Logger logger = LogManager.getLogger(MonitorService.class);

    public static void main(String[] args) throws Exception {
        BeanFactory context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
        final MonitorService monitorService = (MonitorService) context.getBean("server");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("ShutDown called...");
                monitorService.stopService();
            }
        });
        monitorService.startService();
    }
}
