package org.apache.airavata.datastore.models;

import java.util.Date;

public class FileMetadata {

    private String fileName;

    private String filePath;

    private String generatedApplicationName;

    private Date createdDate;

    private Date modifiedDate;

    private String ownerName;

    private String ownerGroup;

    private boolean ownerGroupHasAccess;

    private boolean worldHasAccess;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGeneratedApplicationName() {
        return generatedApplicationName;
    }

    public void setGeneratedApplicationName(String generatedApplicationName) {
        this.generatedApplicationName = generatedApplicationName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerGroup() {
        return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public boolean isOwnerGroupHasAccess() {
        return ownerGroupHasAccess;
    }

    public void setOwnerGroupHasAccess(boolean ownerGroupHasAccess) {
        this.ownerGroupHasAccess = ownerGroupHasAccess;
    }

    public boolean isWorldHasAccess() {
        return worldHasAccess;
    }

    public void setWorldHasAccess(boolean worldHasAccess) {
        this.worldHasAccess = worldHasAccess;
    }
}
