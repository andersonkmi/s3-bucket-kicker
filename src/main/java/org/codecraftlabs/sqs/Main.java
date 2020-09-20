package org.codecraftlabs.sqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.sqs.util.CommandLineException;
import org.codecraftlabs.sqs.util.CommandLineUtil;
import org.codecraftlabs.sqs.validator.InvalidArgumentException;

import static java.lang.Thread.sleep;
import static org.codecraftlabs.sqs.util.CommandLineUtil.help;
import static org.codecraftlabs.sqs.validator.AppArgsValidator.build;

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
                            // ignore exception
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

            var cliValidator = build();
            cliValidator.setSkipValidation(true);
            cliValidator.validate(arguments);

            var intervalValue = 5;
            logger.info(String.format("Pausing for %d seconds", intervalValue));
            sleep(intervalValue * 1000);

            if (isVMShuttingDown) {
                signalReadyToExit();
            }
            logger.info("Finishing app");
            unregisterShutdownHook();
        } catch (InvalidArgumentException |
                 IllegalArgumentException |
                 CommandLineException |
                 InterruptedException exception) {
            logger.error("Failed to parse command line options", exception);
            help();
        }
    }

    public static void main(String[] args) {
        new Main().start(args);
    }
}
