package org.apache.airavata.datastore.parser;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;

import java.io.File;
import java.util.HashMap;

public abstract class Parser implements Runnable{

    protected FileWatcherMessage fileWatcherMessage;

    public abstract HashMap<String, String> parse();

    public void setFileWatcherMessage(FileWatcherMessage fileWatcherMessage){
        this.fileWatcherMessage = fileWatcherMessage;
    }

    @Override
    public void run(){
        this.parse();
    }
}
