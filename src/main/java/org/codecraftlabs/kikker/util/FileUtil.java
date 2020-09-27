package org.codecraftlabs.kikker.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    public static Map<String, String> listFiles(String folder, String fileExtension) {
        var result = new HashMap<String, String>();

        var rootFolder = new File(folder);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return result;
        }

        var matchedFiles = rootFolder.list((dir, name) -> name.endsWith(fileExtension));
        if (matchedFiles != null) {
            Arrays.stream(matchedFiles).forEach(item -> {
                var fullPath = prependFolderName(folder, item);
                result.put(item, fullPath);
            });
        }
        return result;
    }

    private static String prependFolderName(@Nonnull final String folderName, @Nonnull final String fileName) {
        if (folderName.endsWith(File.separator)) {
            return folderName + fileName;
        }
        return folderName + File.separator + fileName;
    }
}
