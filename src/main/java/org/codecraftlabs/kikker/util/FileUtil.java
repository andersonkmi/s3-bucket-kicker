package org.codecraftlabs.kikker.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtil {
    public List<String> listFiles(String folder, String fileExtension) {
        var rootFolder = new File(folder);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            return Collections.emptyList();
        }

        var matchedFiles = rootFolder.list((dir, name) -> name.endsWith(fileExtension));
        return matchedFiles != null ? Arrays.asList(matchedFiles) : Collections.emptyList();
    }

    private String prependFolderName(@Nonnull final String folderName, @Nonnull final String fileName) {
        return "";
    }
}
