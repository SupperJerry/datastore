package org.apache.airavata.datastore.parser.impl;

import org.apache.airavata.datastore.models.FileMetadata;
import org.apache.airavata.datastore.parser.Parser;

import java.io.File;
import java.util.HashMap;

public class DummyParser extends Parser {

    @Override
    public FileMetadata parse() {
        if(fileWatcherMessage!=null) {
            File file = new File(fileWatcherMessage.getFilePath() + File.pathSeparator + fileWatcherMessage.getFileName());

            //metadata generation should be done. this is just example
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Key1", "Val1");
            map.put("Key2", "Val2");
            map.put("Key3", "Val3");
            map.put("Key4", "Val4");
            map.put("Key5", "Val5");
            map.put("Key6", "Val6");

            return new FileMetadata();
        }
        return null;
    }
}
