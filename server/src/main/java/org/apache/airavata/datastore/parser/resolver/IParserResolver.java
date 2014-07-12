package org.apache.airavata.datastore.parser.resolver;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.Parser;

public interface IParserResolver {
    public Parser getParser(FileWatcherMessage fileWatcherMessage);
}
