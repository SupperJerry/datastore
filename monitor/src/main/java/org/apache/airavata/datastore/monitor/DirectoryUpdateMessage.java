package org.apache.airavata.datastore.monitor;

import java.nio.file.Path;

public class DirectoryUpdateMessage {

    private Path fileName;

    private Path parentPath;


    public DirectoryUpdateMessage(Path fileName, Path filePath) {
        this.fileName = fileName;
        this.parentPath = filePath;
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
