package org.apache.airavata.datastore.monitor;

import java.io.IOException;
import java.nio.file.Path;

public interface IMonitor {

    /**
     * Start directory monitoring
     */
    public void startMonitor(Path path) throws IOException;

    /**
     * Stop directory monitoring
     */
    public void stopMonitor();
}
