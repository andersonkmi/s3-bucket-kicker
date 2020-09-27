package org.codecraftlabs.kikker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.kikker.service.AWSException;
import org.codecraftlabs.kikker.service.S3Service;
import org.codecraftlabs.kikker.util.CommandLineException;
import org.codecraftlabs.kikker.util.CommandLineUtil;
import org.codecraftlabs.kikker.validator.InvalidArgumentException;

import java.util.Map;

import static java.lang.Thread.sleep;
import static org.codecraftlabs.kikker.util.CommandLineArguments.FILE_EXTENSION;
import static org.codecraftlabs.kikker.util.CommandLineArguments.INPUT_FOLDER;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_BUCKET;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_PREFIX;
import static org.codecraftlabs.kikker.util.CommandLineUtil.help;
import static org.codecraftlabs.kikker.util.FileUtil.listFiles;
import static org.codecraftlabs.kikker.validator.AppArgsValidator.build;

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
                        logger.info("Control-C detected. Terminating process, please wait.");
                        try {
                            // Wait up to 1.5 secs for a record to be processed.
                            wait(1500);
                        } catch (InterruptedException ignore) {
                            logger.warn("Interruption exception", ignore);
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
        logger.warn("Unregistering shutdown hook");
        try {
            Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        } catch (IllegalStateException ignore) {
            // VM already was shutting down
        }
    }

    public void start(String[] args) {
        logger.info("Starting app");
        try {
            var commandLineUtil = new CommandLineUtil();
            var arguments = commandLineUtil.parse(args);

            var cliValidator = build(true);
            cliValidator.validate(arguments);

            var s3Service = new S3Service();

            var intervalValue = 5;
            // Insert the logic here
            var filesToUpload = listFiles(arguments.option(INPUT_FOLDER), arguments.option(FILE_EXTENSION));
            var entries = filesToUpload.entrySet();

            for (Map.Entry<String, String> entry : entries) {
                try {
                    logger.info(String.format("Uploading '%s' to bucket '%s'", entry.getValue(), arguments.option(S3_BUCKET) + "/" + arguments.option(S3_PREFIX) + "/" + entry.getKey()));
                    s3Service.upload(arguments.option(S3_BUCKET), arguments.option(S3_PREFIX) + "/" + entry.getKey(), entry.getValue());
                } catch (AWSException exception) {
                    logger.error("Failed to upload file: " + entry.getValue(), exception);
                }
            }

            logger.info(String.format("Pausing for %d seconds", intervalValue));
            sleep(intervalValue * 1000);

            if (isVMShuttingDown) {
                signalReadyToExit();
            }
            logger.info("Finishing app");
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
