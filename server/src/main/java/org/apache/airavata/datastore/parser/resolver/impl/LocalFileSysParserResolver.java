package org.apache.airavata.datastore.parser.resolver.impl;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.airavata.datastore.parser.impl.DummyParser;
import org.apache.airavata.datastore.parser.resolver.IParserResolver;


public class LocalFileSysParserResolver implements IParserResolver{
    @Override
    public IParser createParser(FileWatcherMessage fileWatcherMessage) {
        return new DummyParser();
    }
}
