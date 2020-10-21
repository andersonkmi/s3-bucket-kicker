package org.codecraftlabs.kikker.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.kikker.util.CommandLineArguments;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

public class AppArgsValidator {
    private static final Logger logger = LogManager.getLogger(AppArgsValidator.class);
    private final Set<AppArgumentsValidationPolicy> policies = new LinkedHashSet<>();
    private boolean skipValidation;

    private AppArgsValidator(boolean skipValidation) {
        this.skipValidation = skipValidation;
        policies.add(new InputFolderValidationPolicy());
    }

    public static AppArgsValidator build(boolean skipValidation) {
        return new AppArgsValidator(skipValidation);
    }

    public void validate(@Nonnull CommandLineArguments args) throws InvalidArgumentException {
        logger.info("Command line validation in progress");

        if (skipValidation) {
            return;
        }

        for (AppArgumentsValidationPolicy policy : policies) {
            policy.verify(args);
        }
    }
}
