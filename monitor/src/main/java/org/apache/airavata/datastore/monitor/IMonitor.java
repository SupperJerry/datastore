package org.apache.airavata.datastore.monitor;

public interface IMonitor {

    /**
     * Start directory monitoring
     */
    public void startMonitor();

    /**
     * Stop directory monitoring
     */
    public void stopMonitor();

    /**
     * Processing directory update events
     */
    public void processEvents();

}
