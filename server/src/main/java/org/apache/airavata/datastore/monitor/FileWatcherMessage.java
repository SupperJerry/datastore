package org.apache.airavata.datastore.monitor;

import java.nio.file.Path;

public class FileWatcherMessage {

    private Path fileName;

    private Path parentPath;

    private String eventType;

    public FileWatcherMessage(Path fileName, Path parentPath, String eventType) {
        this.fileName = fileName;
        this.parentPath = parentPath;
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Path getParentPath() {
        return parentPath;
    }

    public void setParentPath(Path parentPath) {
        this.parentPath = parentPath;
    }

    public Path getFileName() {
        return fileName;
    }

    public void setFileName(Path fileName) {
        this.fileName = fileName;
    }
}
