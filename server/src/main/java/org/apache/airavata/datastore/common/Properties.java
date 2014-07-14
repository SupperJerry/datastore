package org.apache.airavata.datastore.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Properties {
    private final Logger logger = LogManager.getLogger(Properties.class);

    private static Properties props = null;

    public static final String SERVER_PROPERTY_FILE = "../conf/server.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/server.properties";

    public static final String LOG4J_PROPERTY_FILE = "../conf/log4j.properties";

    private static final String DATA_ROOT = "data.root";
    private static final String MAX_PARSER_THREADS = "dispatcher.max_parser_threads";
    private static final String BATCH_MONITOR_WAIT_TIME = "batch_monitor.wait_time";

    private String dataRoot = null;
    private int maxParserThreads;
    private long waitTime;

    private Properties() {
        this.loadServerProperties();
    }

    public static Properties getInstance() {
        if (props == null) {
            props = new Properties();
        }
        return props;
    }

    private InputStream getServerPropertiesURL() throws Exception {
        InputStream file = null;

        // load server.properties from conf directory.
        if (new File(SERVER_PROPERTY_FILE).exists()) {
            file = new FileInputStream(SERVER_PROPERTY_FILE);
        } else {
            // try to load default parser.properties from class loader.
            file = ClassLoader.getSystemResource(DEFAULT_SERVER_PROPERTY_FILE).openStream();
        }

        return file;
    }

    private void loadServerProperties(){
        java.util.Properties properties = new java.util.Properties();
        InputStream file;
        try {
            file = getServerPropertiesURL();
            properties.load(file);
            dataRoot = (String) properties.get(DATA_ROOT);
            maxParserThreads = Integer.parseInt((String)properties.get(MAX_PARSER_THREADS));
            waitTime = Long.parseLong((String)properties.get(BATCH_MONITOR_WAIT_TIME));
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public String getDataRoot() {
        return dataRoot;
    }

    public int getMaxParserThreads() { return maxParserThreads; }

    public long getWaitTime() {
        return waitTime;
    }
}
