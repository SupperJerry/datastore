package org.apache.airavata.datastore.parser;

import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;

import java.util.HashMap;

public abstract class Parser{

    protected FileMonitorMessage fileWatcherMessage;

    public abstract FileMetadata parse(FileMonitorMessage fileMonitorMessage);

}
