package org.codecraftlabs.sqs.util;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.codecraftlabs.sqs.util.AppArguments.INTERVAL_SECONDS_OPTION;
import static org.codecraftlabs.sqs.util.AppArguments.OPERATION_OPTION;
import static org.codecraftlabs.sqs.util.AppArguments.SQS_URL_OPTION;

public class CommandLineUtil {
    private CommandLineParser commandLineParser;

    private static final Logger logger = LogManager.getLogger(CommandLineUtil.class);

    public static final String SQS_URL_OPT = "s";
    public static final String INTERVAL_SECONDS_OPT = "i";
    public static final String OPERATION_OPT = "o";

    final private static Options cmdLineOpts = new Options().addRequiredOption(SQS_URL_OPT, SQS_URL_OPTION, true, "SQS url")
            .addRequiredOption(OPERATION_OPT, OPERATION_OPTION, true, "Operation to be performed")
            .addRequiredOption(INTERVAL_SECONDS_OPT, INTERVAL_SECONDS_OPTION, true, "Interval in seconds");

    public CommandLineUtil() {
        commandLineParser = new DefaultParser();
    }

    public AppArguments parse(String[] args) throws CommandLineException {
        logger.info("Parsing command line arguments");

        final Map<String, String> options = new HashMap<>();

        try {
            var cmdLine = commandLineParser.parse(cmdLineOpts, args);
            options.put(SQS_URL_OPTION, cmdLine.getOptionValue(SQS_URL_OPT));
            options.put(INTERVAL_SECONDS_OPTION, cmdLine.getOptionValue(INTERVAL_SECONDS_OPT));
            options.put(OPERATION_OPTION, cmdLine.getOptionValue(OPERATION_OPT));

            return new AppArguments(options);
        } catch (ParseException exception) {
            logger.error("Command line parse error", exception);
            throw new CommandLineException("Error when parsing command line options", exception);
        }
    }

    public static void help() {
        var header = "\nAWS SQS producer sandbox app\n";
        var footer = "\nThank you for using\n";
        new HelpFormatter().printHelp("java -jar aws-sqs-producer-all.jar", header, cmdLineOpts, footer, true);
    }
}
