package org.apache.airavata.datastore.parser.resolver;

import org.apache.airavata.datastore.models.FileMonitorMessage;
import org.apache.airavata.datastore.parser.Parser;

public interface IParserResolver {
    public Parser getParser(FileMonitorMessage fileWatcherMessage);
}
