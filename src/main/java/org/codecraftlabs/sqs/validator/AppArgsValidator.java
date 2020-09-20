package org.codecraftlabs.sqs.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.sqs.util.AppArguments;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;
import java.util.Set;

public class AppArgsValidator {
    private static final Logger logger = LogManager.getLogger(AppArgsValidator.class);
    private final Set<AppArgumentsValidationPolicy> policies = new LinkedHashSet<>();

    private AppArgsValidator() {
    }

    public static AppArgsValidator build() {
        return new AppArgsValidator();
    }

    public void validate(@Nonnull AppArguments args) throws InvalidArgumentException {
        logger.info("Command line validation in progress");

        for (AppArgumentsValidationPolicy policy : policies) {
            policy.verify(args);
        }
    }
}
