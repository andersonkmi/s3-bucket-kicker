package org.codecraftlabs.kikker.util;

import java.util.Map;

public class CommandLineArguments {
    public static final String INPUT_FOLDER = "input-folder";
    public static final String S3_BUCKET = "bucket";
    public static final String S3_PREFIX = "prefix";
    public static final String FILE_EXTENSION = "file-extension";

    private Map<String, String> arguments;

    public CommandLineArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String option(String key) {
        return arguments.getOrDefault(key, "");
    }
}
