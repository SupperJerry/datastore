package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class AutoDetectParser implements IParser{

    @Autowired
    private IParserResolver iParserResolver;

    public HashMap<String, String> parse(FileWatcherMessage fileWatcherMessage){
        if(fileWatcherMessage.getEventType() == Constants.FILE_CREATED){
            IParser parser = null;
            try {
                parser = iParserResolver.getParser(fileWatcherMessage);
                if(parser!=null){
                    return parser.parse(fileWatcherMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
}
