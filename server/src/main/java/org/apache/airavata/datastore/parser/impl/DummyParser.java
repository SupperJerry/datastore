package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.monitor.FileWatcherMessage;
import org.apache.airavata.datastore.parser.IParser;

import java.io.File;
import java.util.HashMap;

public class DummyParser implements IParser {
    @Override
    public HashMap<String, String> parse(FileWatcherMessage fileWatcherMessage) {
        File file = new File(fileWatcherMessage.getParentPath()+File.pathSeparator+fileWatcherMessage.getFileName());

        //metadata generation should be done. this is just example
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Key1", "Val1");
        map.put("Key2", "Val2");
        map.put("Key3", "Val3");
        map.put("Key4", "Val4");
        map.put("Key5", "Val5");
        map.put("Key6", "Val6");

        return map;
    }
}
