package org.codecraftlabs.kikker.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {
    public List<String> listFiles(String folder, String fileExtension) {
        var rootFolder = new File(folder);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return Collections.emptyList();
        }

        var matchedFiles = rootFolder.list((dir, name) -> name.endsWith(fileExtension));
        return matchedFiles != null ?
                Arrays.stream(matchedFiles).map(item -> prependFolderName(folder, item)).collect(Collectors.toList()) :
                Collections.emptyList();
    }

    private String prependFolderName(@Nonnull final String folderName, @Nonnull final String fileName) {
        if (folderName.endsWith(File.pathSeparator)) {
            return folderName + fileName;
        }
        return folderName + File.pathSeparator + fileName;
    }
}
