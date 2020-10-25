package org.codecraftlabs.kikker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileUploadManager {
    private static final Logger logger = LogManager.getLogger(FileUploadManager.class);

    private final String controlFile;
    private Set<String> uploadedFiles;

    public FileUploadManager() {
        controlFile = ".upload-control.txt";
        uploadedFiles = new HashSet<>();

        // Loads the upload control file
        File uploadControlFile = new File(controlFile);
        if (uploadControlFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(uploadControlFile.getName()));
                String line;
                while ((line = reader.readLine()) != null) {
                    uploadedFiles.add(line);
                }
                reader.close();
            } catch (IOException exception) {
                logger.warn("Error when opening the control file", exception);
            }
        }
    }
}
