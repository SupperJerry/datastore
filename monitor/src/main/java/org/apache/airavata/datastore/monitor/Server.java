package org.apache.airavata.datastore.monitor;

import org.apache.airavata.datastore.monitor.common.ServerProperties;
import org.apache.airavata.datastore.monitor.dispatcher.Dispatcher;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private IMonitor monitor;
    private Dispatcher dispatcher;


    public Server() throws Exception {
        dispatcher =  new Dispatcher();
    }

    @Autowired
    public void setMonitor(IMonitor monitorImpl) throws Exception{
        monitor = monitorImpl;
    }

    public void startService() throws Exception {
        //Loading Log4J property from External Source
        if (new File(ServerProperties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(ServerProperties.LOG4J_PROPERTY_FILE);
        }

        System.out.println("\nStarting DataStore Server...!\n");

        //Starting directory monitor
        ServerProperties properties = ServerProperties.getInstance();
        monitor.startMonitor(properties.getDataRoot());

        //Starting directory update message dispatcher
        dispatcher.startDispatcher();
    }

    public void stopService(){
        monitor.stopMonitor();
        System.out.println("\nGood bye from DataStore Server...!\n");
    }

    public static void main(String[] args) throws Exception {
        ServerProperties props = ServerProperties.getInstance();
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
        BeanFactory factory = context;
        final Server monitorServer = (Server) factory.getBean("server");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("ShutDown called...");
                monitorServer.stopService();
            }
        });
        monitorServer.startService();
    }
}
