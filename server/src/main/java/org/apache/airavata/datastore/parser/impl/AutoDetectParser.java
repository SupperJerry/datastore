package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

public class AutoDetectParser implements IParser{

    @Autowired
    private IParserResolver parserResolver;

    public HashMap<String, String> parse(FileWatcherMessage fileWatcherMessage){
        if(fileWatcherMessage.getEventType() == Constants.FILE_CREATED){
            IParser parser = parserResolver.createParser(fileWatcherMessage);
            return parser.parse(fileWatcherMessage);
        }
        return  null;
    }
}
