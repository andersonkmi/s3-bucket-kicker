package org.codecraftlabs.sqs.validator;

import org.codecraftlabs.sqs.util.AppArguments;

import javax.annotation.Nonnull;

import java.io.File;

import static org.codecraftlabs.sqs.util.AppArguments.INPUT_FOLDER;

class InputFolderValidationPolicy implements AppArgumentsValidationPolicy {
    @Override
    public void verify(@Nonnull AppArguments args) throws InvalidArgumentException {
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
