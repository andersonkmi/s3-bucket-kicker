package org.codecraftlabs.kikker.validator;

import org.codecraftlabs.kikker.util.CommandLineArguments;

import javax.annotation.Nonnull;

import java.io.File;

import static org.codecraftlabs.kikker.util.CommandLineArguments.INPUT_FOLDER;

class InputFolderValidationPolicy implements AppArgumentsValidationPolicy {
    @Override
    public void verify(@Nonnull CommandLineArguments args) throws InvalidArgumentException {
        String folder = args.option(INPUT_FOLDER);

        var inputFolder = new File(folder);
        if (! inputFolder.isDirectory()) {
            throw new InvalidArgumentException("Invalid folder");
        }

        if (! inputFolder.canRead()) {
            throw new InvalidArgumentException("Folder is not accessible");
        }
    }
}
