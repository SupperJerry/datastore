package org.apache.airavata.datastore.parser.resolver;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.monitor.MonitorService;
import org.apache.airavata.datastore.parser.Parser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;


public class LocalFileSysParserResolver implements IParserResolver{

    private static final Logger logger = LogManager.getLogger(MonitorService.class);

    public static final String PARSER_PROPERTY_FILE = "../conf/parser.properties";
    public static final String DEFAULT_PARSER_PROPERTY_FILE = "conf/parser.properties";

    private Properties properties;

    public LocalFileSysParserResolver() throws IOException {
        InputStream file = null;

        // load server.properties from conf directory.
        if (new File(PARSER_PROPERTY_FILE).exists()) {
            file = new FileInputStream(PARSER_PROPERTY_FILE);
        } else {
            // try to load default parser.properties from class loader.
            file = ClassLoader.getSystemResource(DEFAULT_PARSER_PROPERTY_FILE).openStream();
        }
        properties = new java.util.Properties();
        properties.load(file);
    }

    @Override
    public Parser getParser(FileWatcherMessage fileWatcherMessage){
        String className = (String) properties.get((new File(fileWatcherMessage.getFilePath()).getParent().toString()));
        if(!className.isEmpty()) {
            try {
                return (Parser) Class.forName(className).newInstance();
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        logger.error("No entry in the properties file for the directory: "+fileWatcherMessage.getFilePath());
        return null;
    }
}
