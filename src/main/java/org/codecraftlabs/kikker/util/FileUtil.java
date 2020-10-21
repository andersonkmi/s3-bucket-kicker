package org.codecraftlabs.kikker.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    @Nonnull
    public static Map<String, String> listFiles(@Nonnull final String folder, @Nonnull final String fileExtension) {
        Map<String, String> result = new HashMap<>();

        var rootFolder = new File(folder);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return result;
        }

        String[] matchedFiles = rootFolder.list((dir, name) -> name.endsWith(fileExtension));
        if (matchedFiles != null) {
            Arrays.stream(matchedFiles).forEach(item -> {
                String fullPath = prependFolderName(folder, item);
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
