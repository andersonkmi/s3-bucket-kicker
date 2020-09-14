package org.codecraftlabs.sqs.util;

import java.util.Map;

public class AppArguments {
    public static final String SOURCE_FOLDER = "input-folder";
    public static final String S3_BUCKET = "bucket";
    public static final String S3_PREFIX = "prefix";

    private Map<String, String> arguments;

    public AppArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String option(String key) {
        return arguments.getOrDefault(key, "");
    }
}
