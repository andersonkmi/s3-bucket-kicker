package org.codecraftlabs.kikker.util;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static org.codecraftlabs.kikker.util.CommandLineArguments.FILE_EXTENSION;
import static org.codecraftlabs.kikker.util.CommandLineArguments.INPUT_FOLDER;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_BUCKET;
import static org.codecraftlabs.kikker.util.CommandLineArguments.S3_PREFIX;

public class CommandLineUtil {
    private final CommandLineParser commandLineParser;

    private static final Logger logger = LogManager.getLogger(CommandLineUtil.class);

    private static final String INPUT_FOLDER_OPTION = "i";
    private static final String EXTENSION_OPTION = "e";
    private static final String S3_BUCKET_OPTION = "b";
    private static final String S3_PREFIX_OPTION = "p";

    final private static Options cmdLineOpts = new Options().addRequiredOption(INPUT_FOLDER_OPTION, INPUT_FOLDER, true, "Input folder")
            .addRequiredOption(EXTENSION_OPTION, FILE_EXTENSION, true, "File extension")
            .addRequiredOption(S3_BUCKET_OPTION, S3_BUCKET, true, "S3 bucket to upload the files")
            .addRequiredOption(S3_PREFIX_OPTION, S3_PREFIX, true, "S3 bucket prefix");

    public CommandLineUtil() {
        commandLineParser = new DefaultParser();
    }

    @Nonnull
    public CommandLineArguments parse(@Nonnull String[] args) throws CommandLineException {
        logger.info("Parsing command line arguments");

        final Map<String, String> options = new HashMap<>();

        try {
            var cmdLine = commandLineParser.parse(cmdLineOpts, args);
            options.put(INPUT_FOLDER, cmdLine.getOptionValue(INPUT_FOLDER_OPTION));
            options.put(S3_BUCKET, cmdLine.getOptionValue(S3_BUCKET_OPTION));
            options.put(S3_PREFIX, cmdLine.getOptionValue(S3_PREFIX_OPTION));
            options.put(FILE_EXTENSION, cmdLine.getOptionValue(EXTENSION_OPTION));
            return new CommandLineArguments(options);
        } catch (ParseException exception) {
            throw new CommandLineException("Error when parsing command line options", exception);
        }
    }

    public static void help() {
        var header = "\nAWS S3 uploader\n";
        var footer = "\nThank you for using\n";
        new HelpFormatter().printHelp("java -jar aws-s3-kikker-all.jar", header, cmdLineOpts, footer, true);
    }
}
