package org.apache.airavata.datastore.monitor.impl;

import org.apache.airavata.datastore.common.Constants;
import org.apache.airavata.datastore.common.Properties;
import org.apache.airavata.datastore.models.FileMonitorMessage;
import org.apache.airavata.datastore.monitor.IMonitor;
import org.apache.airavata.datastore.orchestrator.dispatcher.MonitorDispatcherQueue;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalFileSysBatchMonitor implements IMonitor{
    private final Logger logger = LogManager.getLogger(LocalFileSysBatchMonitor.class);
    private ArrayList<FileInfo> snapshot;
    private Properties properties;
    private long waitTime;
    private boolean runMonitor;

    @Autowired
    private MonitorDispatcherQueue monitorDispatcherQueue;

    private String serialisedFileName = "snapshot.ser";

    public LocalFileSysBatchMonitor() throws Exception {
        properties = Properties.getInstance();
        waitTime = properties.getWaitTime();
        runMonitor = false;
        if((new File(serialisedFileName)).exists()){
            try {
                snapshot = (ArrayList<FileInfo>) (new ObjectInputStream(new FileInputStream(serialisedFileName))).readObject();
            } catch (IOException e) {
                logger.error(e.toString());
            } catch (ClassNotFoundException e) {
                logger.error(e.toString());
            }
        }else{
            ArrayList<FileInfo> currentFiles = getCurrentFilesList(properties.getDataRoot());
            ArrayList<FileMonitorMessage> fileWatcherMessages = new ArrayList<FileMonitorMessage>();
            for(int i=0;i<currentFiles.size();i++){
                FileMonitorMessage fileWatcherMessage = new FileMonitorMessage(currentFiles.get(i).getFileName(),
                        currentFiles.get(i).getFilePath(), Constants.FILE_CREATED
                );

                fileWatcherMessages.add(fileWatcherMessage);
            }

            //@Todo
            //Implement batch update
            for(int i=0;i<fileWatcherMessages.size();i++){
                monitorDispatcherQueue.addMsgToQueue(fileWatcherMessages.get(i));
            }

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(serialisedFileName));
            outputStream.writeObject(currentFiles);
            this.snapshot = currentFiles;
        }

    }

    /**
     * Start directory monitoring
     */
    @Override
    public void startMonitor(Path path) throws IOException {
        runMonitor = true;
        processEvents();
        logger.info("Started directory watching");
    }

    /**
     * Stop directory monitoring
     */
    @Override
    public void stopMonitor() {
        runMonitor = false;
        logger.info("Stopped directory watching");
    }

    private void processEvents(){
        (new Thread(new Runnable(){
            @Override
            public void run() {
                while(runMonitor){
                    try {
                        logger.info("Starting Batch Monitor Scan...");
                        Thread.sleep(waitTime);
                        HashMap<String, FileInfo> currentFilesMap = getCurrentFilesMap(properties.getDataRoot());
                        ArrayList<FileInfo> currentFilesList = getCurrentFilesList(properties.getDataRoot());
                        ArrayList<FileMonitorMessage> fileWatcherMessages = new ArrayList<FileMonitorMessage>();
                        for(int i=0;i<snapshot.size();i++){
                            FileInfo fSnap = snapshot.get(i);
                            FileInfo fCurrent  = currentFilesMap.get(fSnap.getFilePath());
                            if(fCurrent==null){
                                FileMonitorMessage fileWatcherMessage = new FileMonitorMessage(fSnap.getFileName(),
                                        fSnap.getFilePath(), Constants.FILE_DELETED);
                                fileWatcherMessages.add(fileWatcherMessage);
                            }else{
                                if(fSnap.getLastModifiedTime()!=fCurrent.getLastModifiedTime()){
                                    FileMonitorMessage fileWatcherMessage = new FileMonitorMessage(fSnap.getFileName(),
                                            fSnap.getFilePath(), Constants.FILE_MODIFIED);
                                    fileWatcherMessages.add(fileWatcherMessage);
                                }
                                currentFilesMap.remove(fSnap.getFilePath());
                            }
                        }
                        ArrayList<FileInfo> newlyCreatedFiles = new ArrayList<FileInfo>(currentFilesMap.values());
                        for(int i=0;i<newlyCreatedFiles.size();i++){
                            FileInfo fNew = newlyCreatedFiles.get(i);
                            FileMonitorMessage fileWatcherMessage = new FileMonitorMessage(fNew.getFileName(),
                                    fNew.getFilePath(), Constants.FILE_MODIFIED);
                            fileWatcherMessages.add(fileWatcherMessage);
                        }
                        logger.info("Ending Batch Monitor Scan...");

                        //@Todo
                        //Implement batch update
                        for(int i=0;i<fileWatcherMessages.size();i++){
                            monitorDispatcherQueue.addMsgToQueue(fileWatcherMessages.get(i));
                        }

                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(serialisedFileName));
                            outputStream.writeObject(currentFilesList);
                        } catch (IOException e) {
                            logger.error(e.toString());
                        }
                        logger.info("Successfully serialized new directory snapshot...");
                        snapshot = currentFilesList;

                    } catch (InterruptedException e) {
                        logger.error(e.toString());
                    }
                }
            }
        })).start();
    }

    private HashMap<String, FileInfo> getCurrentFilesMap(String path){
        HashMap<String, FileInfo> currentFiles = new HashMap<String, FileInfo>();
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return currentFiles;

        for ( File f : list ) {
            if ( !f.isDirectory() ) {
                FileInfo newFileInfo = new FileInfo(f.getName(),f.getPath(),f.lastModified());
                currentFiles.put(f.getPath(),newFileInfo);
            }
        }

        return currentFiles;
    }


    private ArrayList<FileInfo> getCurrentFilesList(String path){
        ArrayList<FileInfo> currentFiles = new ArrayList<FileInfo>();
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return currentFiles;

        for ( File f : list ) {
            if ( !f.isDirectory() ) {
                FileInfo newFileInfo = new FileInfo(f.getName(),f.getPath(),f.lastModified());
                currentFiles.add(newFileInfo);
            }
        }

        return currentFiles;
    }
}


class FileInfo{

    private String fileName;

    private String filePath;

    private long lastModifiedTime;

    FileInfo(String fileName, String filePath, long lastModified) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.lastModifiedTime = lastModified;
    }

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

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModified) {
        this.lastModifiedTime = lastModified;
    }
}