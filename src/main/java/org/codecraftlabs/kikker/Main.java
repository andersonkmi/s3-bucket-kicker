package org.codecraftlabs.kikker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.kikker.service.AWSException;
import org.codecraftlabs.kikker.service.S3Service;
import org.codecraftlabs.kikker.util.CommandLineArguments;
import org.codecraftlabs.kikker.util.CommandLineException;
import org.codecraftlabs.kikker.util.CommandLineUtil;
import org.codecraftlabs.kikker.util.FileUploadManager;
import org.codecraftlabs.kikker.validator.AppArgsValidator;
import org.codecraftlabs.kikker.validator.InvalidArgumentException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;
import static org.codecraftlabs.kikker.util.CommandLineArguments.FILE_EXTENSION;
import static org.codecraftlabs.kikker.util.CommandLineArguments.INPUT_FOLDER;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_BUCKET;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_PREFIX;
import static org.codecraftlabs.kikker.util.CommandLineUtil.help;
import static org.codecraftlabs.kikker.util.FileUtil.listFiles;
import static org.codecraftlabs.kikker.validator.AppArgsValidator.build;
import static software.amazon.awssdk.regions.Region.US_EAST_1;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private boolean isVMShuttingDown = false;
    private boolean readyToExit = false;
    private Thread shutdownThread;

    public Main() {
        registerShutdownHook();
    }

    private synchronized void signalReadyToExit() {
        this.readyToExit = true;
        this.notify();
    }

    private void registerShutdownHook() {
        this.shutdownThread = new Thread("ShutdownHook") {
            public void run() {
                synchronized(this) {
                    if (!readyToExit) {
                        isVMShuttingDown = true;
                        try {
                            // Wait up to 1.5 secs for a record to be processed.
                            wait(1500);
                        } catch (InterruptedException exception) {
                            //logger.warn("Interruption exception", ignore);
                        }

                        if (!readyToExit) {
                            logger.info("Main processing interrupted.");
                        }
                    }
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
    }

    /** Unregister the shutdown hook. */
    private void unregisterShutdownHook() {
        try {
            Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        } catch (IllegalStateException ignore) {
            // VM already was shutting down
        }
    }

    public void start(String[] args) {
        Instant start = Instant.now();
        logger.info("Starting app");
        try {
            CommandLineUtil commandLineUtil = new CommandLineUtil();
            CommandLineArguments arguments = commandLineUtil.parse(args);

            AppArgsValidator cliValidator = build(true);
            cliValidator.validate(arguments);

            S3Service s3Service = new S3Service(US_EAST_1);

            int intervalValue = 5;
            Map<String, String> filesToUpload = listFiles(arguments.option(INPUT_FOLDER), arguments.option(FILE_EXTENSION));
            Set<Map.Entry<String, String>> entries = filesToUpload.entrySet();

            List<Map.Entry<String, String>> retryList = new ArrayList<>();

            // Regular processing
            FileUploadManager fileUploadManager = new FileUploadManager();
            for (Map.Entry<String, String> entry : entries) {
                try {
                    if (fileUploadManager.isFileAlreadyProcessed(entry.getValue())) {
                        logger.warn(String.format("File already uploaded: '%s' - skipping", entry.getValue()));
                        continue;
                    }

                    logger.info(String.format("Uploading '%s' to bucket '%s'", entry.getValue(), "s3://" + arguments.option(S3_BUCKET) + "/" + arguments.option(S3_PREFIX) + "/" + entry.getKey()));
                    s3Service.upload(arguments.option(S3_BUCKET), arguments.option(S3_PREFIX) + "/" + entry.getKey(), entry.getValue());

                    fileUploadManager.add(entry.getValue());
                } catch (AWSException exception) {
                    logger.error("Failed to upload file: " + entry.getValue(), exception);
                    retryList.add(entry);
                }
            }

            // Process retry list if needed
            if (! retryList.isEmpty()) {
                logger.info(String.format("Pausing for %d seconds before processing retry list", intervalValue));
                sleep(intervalValue * 1000);
                for (Map.Entry<String, String> entry : retryList) {
                    try {
                        logger.info(String.format("Uploading '%s' to bucket '%s'", entry.getValue(), "s3://" + arguments.option(S3_BUCKET) + "/" + arguments.option(S3_PREFIX) + "/" + entry.getKey()));
                        s3Service.upload(arguments.option(S3_BUCKET), arguments.option(S3_PREFIX) + "/" + entry.getKey(), entry.getValue());
                        fileUploadManager.add(entry.getValue());
                    } catch (AWSException exception) {
                        logger.error("Failed to upload file again: " + entry.getValue(), exception);
                    }
                }
            }

            fileUploadManager.save();
            logger.info(String.format("Pausing for %d seconds", intervalValue));
            sleep(intervalValue * 1000);

            if (isVMShuttingDown) {
                signalReadyToExit();
            }
            logger.info("Finishing app");
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            logger.info("Time taken: " + timeElapsed.toSeconds() + " seconds");
            unregisterShutdownHook();
        } catch (IllegalArgumentException |
                 InterruptedException exception) {
            logger.error("Failed to parse command line options", exception);
        } catch (CommandLineException | InvalidArgumentException exception) {
            logger.error("Failed to parse command line options");
            help();
        }
    }

    public static void main(String[] args) {
        new Main().start(args);
    }
}
