package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;
import org.apache.airavata.datastore.parser.Parser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class AutoDetectParser extends Parser {
    private final Logger logger = LogManager.getLogger(AutoDetectParser.class);

    @Autowired
    private IParserResolver iParserResolver;

    @Override
    public FileMetadata parse(FileMonitorMessage fileMonitorMessage) {
        if(fileWatcherMessage!=null && fileWatcherMessage.getEventType() == Constants.FILE_CREATED){
            Parser parser = null;
            try {
                parser = iParserResolver.getParser(fileWatcherMessage);
                if(parser!=null){
                    return parser.parse(fileMonitorMessage);
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        return  null;
    }
}
