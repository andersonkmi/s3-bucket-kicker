package org.codecraftlabs.kikker.util;

import javax.annotation.Nonnull;
import java.util.Map;

public class CommandLineArguments {
    public static final String INPUT_FOLDER = "input-folder";
    public static final String S3_BUCKET = "bucket";
    public static final String S3_PREFIX = "prefix";
    public static final String FILE_EXTENSION = "file-extension";

    private final Map<String, String> arguments;

    public CommandLineArguments(@Nonnull final Map<String, String> arguments) {
        this.arguments = arguments;
    }

    @Nonnull
    public String option(@Nonnull final String key) {
        return arguments.getOrDefault(key, "");
    }
}
