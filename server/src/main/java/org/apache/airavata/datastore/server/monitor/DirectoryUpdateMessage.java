package org.apache.airavata.datastore.server.monitor;

public class DirectoryUpdateMessage {
    private String fileName;

    public DirectoryUpdateMessage(String fName){
        this.fileName = fName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
