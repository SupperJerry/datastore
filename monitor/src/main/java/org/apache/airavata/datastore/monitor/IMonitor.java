package org.apache.airavata.datastore.monitor;

import java.io.IOException;

public interface IMonitor {

    /**
     * Start directory monitoring
     */
    public void startMonitor(String path) throws IOException;

    /**
     * Stop directory monitoring
     */
    public void stopMonitor();
}
