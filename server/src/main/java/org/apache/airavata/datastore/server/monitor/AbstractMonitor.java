package org.apache.airavata.datastore.server.monitor;

public abstract class AbstractMonitor{

    /**
     * Start directory monitoring
     */
    public abstract void startMonitor();

    /**
     * Stop directory monitoring
     */
    public abstract void stopMonitor();

    /**
     * Processing directory update events
     */
    public abstract void processEvents();

}
