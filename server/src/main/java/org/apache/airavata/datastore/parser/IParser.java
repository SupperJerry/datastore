package org.apache.airavata.datastore.parser;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;

import java.io.File;
import java.util.HashMap;

public interface IParser {
    public HashMap<String, String> parse(FileWatcherMessage fileWatcherMessage);
}
