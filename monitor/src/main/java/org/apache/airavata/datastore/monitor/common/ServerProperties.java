package org.apache.airavata.datastore.monitor.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ServerProperties {
    private static ServerProperties props = null;

    public static final String SERVER_PROPERTY_FILE = "../conf/monitor.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/monitor.properties";

    public static final String LOG4J_PROPERTY_FILE = "../conf/log4j.properties";

    private static final String DATA_ROOT = "data.root";

    private String dataRoot = null;

    private ServerProperties() throws Exception {
        this.loadServerProperties();
    }

    public static ServerProperties getInstance() throws Exception {
        if (props == null) {
            props = new ServerProperties();
        }
        return props;
    }

    private InputStream getServerPropertiesURL() throws Exception {
        InputStream file = null;

        // load monitor.properties from conf directory.
        if (new File(SERVER_PROPERTY_FILE).exists()) {
            file = new FileInputStream(SERVER_PROPERTY_FILE);
        } else {
            // try to load default monitor.properties from class loader.
            file = ClassLoader.getSystemResource(DEFAULT_SERVER_PROPERTY_FILE).openStream();
        }

        return file;
    }

    private void loadServerProperties() throws Exception {
        Properties properties = new Properties();
        InputStream file = getServerPropertiesURL();
        properties.load(file);
        dataRoot = (String) properties.get(DATA_ROOT);
    }

    public String getDataRoot() {
        return dataRoot;
    }

}
