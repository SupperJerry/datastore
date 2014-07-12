package org.apache.airavata.datastore.parser;

import java.io.File;
import java.util.Map;

public interface IParser {
    public Map<String, String> parse(File file);
}
