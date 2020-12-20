package org.codecraftlabs.kikker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FileUploadManager {
    private static final Logger logger = LogManager.getLogger(FileUploadManager.class);

    private final String controlFile;
    private final Set<String> uploadedFiles;

    public FileUploadManager() {
        String homeFolder = System.getProperty("user.home");
        controlFile = String.format("%s" + File.separator + "%s", homeFolder, ".upload-control.txt");
        uploadedFiles = new HashSet<>();

        logger.info("Upload file manager: " + controlFile);
        // Loads the upload control file
        try {
           File uploadControlFile = new File(controlFile);
           if (uploadControlFile.exists()) {
                logger.info("Loading control file contents");
                    BufferedReader reader = new BufferedReader(new FileReader(controlFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        uploadedFiles.add(line);
                    }
                    reader.close();
            } else {
                boolean result = uploadControlFile.createNewFile();
                if (result) {
                    logger.info("New control file created");
                } else {
                    logger.warn("New control file not created");
                }
            }
        } catch (IOException exception) {
            logger.warn("Error when opening the control file", exception);
        }

    }

    public void add(String fileName) {
        uploadedFiles.add(fileName);
    }

    public boolean isFileAlreadyProcessed(final String fileName) {
        return uploadedFiles.contains(fileName);
    }

    public void save() {
        try {
            Path controlFilePath = Paths.get(controlFile);
            String buffer = uploadedFiles.stream().map(item -> item + "\n").collect(Collectors.joining());
            Files.write(controlFilePath, buffer.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            logger.warn("Problem when saving file", exception);
        }
    }

    private String generateDigest(String file) throws IOException, NoSuchAlgorithmException {
        byte[] contents = Files.readAllBytes(Paths.get(file));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(contents);
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
