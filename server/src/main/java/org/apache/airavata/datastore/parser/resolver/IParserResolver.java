package org.apache.airavata.datastore.parser.resolver;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;
import org.apache.airavata.datastore.parser.impl.DummyParser;

public interface IParserResolver {
    public IParser getParser(FileWatcherMessage fileWatcherMessage);
}
