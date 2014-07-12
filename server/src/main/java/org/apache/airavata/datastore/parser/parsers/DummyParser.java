package org.apache.airavata.datastore.parser.parsers;

import org.apache.airavata.datastore.parser.IParser;

import java.io.File;
import java.util.HashMap;

public class DummyParser implements IParser {
    @Override
    public HashMap<String, String> parse(File file) {
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
