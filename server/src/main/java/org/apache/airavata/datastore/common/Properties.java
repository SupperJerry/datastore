package org.apache.airavata.datastore.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Properties {
    private static Properties props = null;

    public static final String SERVER_PROPERTY_FILE = "../conf/server.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/server.properties";

    public static final String LOG4J_PROPERTY_FILE = "../conf/log4j.properties";

    private static final String DATA_ROOT = "data.root";

    private String dataRoot = null;

    private Properties() throws Exception {
        this.loadServerProperties();
    }

    public static Properties getInstance() throws Exception {
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

    private void loadServerProperties() throws Exception {
        java.util.Properties properties = new java.util.Properties();
        InputStream file = getServerPropertiesURL();
        properties.load(file);
        dataRoot = (String) properties.get(DATA_ROOT);
    }

    public String getDataRoot() {
        return dataRoot;
    }

}
