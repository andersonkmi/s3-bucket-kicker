package org.codecraftlabs.kikker.util;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileUtil {
    public List<String> listFiles(String folder, String fileExtension) {
        var rootFolder = new File(folder);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return Collections.emptyList();
        }
        return null;
    }
}
