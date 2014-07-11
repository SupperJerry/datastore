package org.apache.airavata.datastore.parser;

import org.apache.airavata.datastore.parser.common.ServerProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private ServerProperties props;

    public Server() throws Exception {
        props = ServerProperties.getInstance();
    }

    public void startService() throws Exception {
        //Loading Log4J property from External Source
        if (new File(ServerProperties.LOG4J_PROPERTY_FILE).exists()) {
            PropertyConfigurator.configure(ServerProperties.LOG4J_PROPERTY_FILE);
        }

        System.out.println("\nStarting DataStore Server...!\n");

    }

    public void stopService(){
        System.out.println("\nGood bye from DataStore Server...!\n");
    }

    public static void main(String[] args) throws Exception {
        final Server parserServer = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("ShutDown called...");
                parserServer.stopService();
            }
        });
        parserServer.startService();
    }
}
