package org.apache.airavata.datastore.parser;

import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.models.FileMonitorMessage;

import java.util.HashMap;

public abstract class Parser implements Runnable{

    protected FileMonitorMessage fileWatcherMessage;

    public abstract FileMetadata parse();

    public void setFileWatcherMessage(FileMonitorMessage fileWatcherMessage){
        this.fileWatcherMessage = fileWatcherMessage;
    }

    @Override
    public void run(){
        this.parse();
    }
}
