package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.parser.Parser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class AutoDetectParser extends Parser {

    @Autowired
    private IParserResolver iParserResolver;

    @Override
    public FileMetadata parse() {
        if(fileWatcherMessage!=null && fileWatcherMessage.getEventType() == Constants.FILE_CREATED){
            Parser parser = null;
            try {
                parser = iParserResolver.getParser(fileWatcherMessage);
                parser.setFileWatcherMessage(fileWatcherMessage);
                if(parser!=null){
                    return parser.parse();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
}
