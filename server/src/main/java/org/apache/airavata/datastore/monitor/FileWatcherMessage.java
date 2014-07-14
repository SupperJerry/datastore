package org.apache.airavata.datastore.monitor;

import java.nio.file.Path;

public class FileWatcherMessage {

    private String fileName;

    private String filePath;

    private String eventType;

    public FileWatcherMessage(String fileName, String filePath, String eventType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
