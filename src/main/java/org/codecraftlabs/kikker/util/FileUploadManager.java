package org.codecraftlabs.kikker.util;

import java.util.HashSet;
import java.util.Set;

public class FileUploadManager {
    private final String controlFile;
    private Set<String> uploadedFiles;

    public FileUploadManager() {
        controlFile = ".upload-control.txt";
        uploadedFiles = new HashSet<>();

        // Loads the upload control file
    }
}
