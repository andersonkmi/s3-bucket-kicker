package org.codecraftlabs.sqs.validator;

import org.codecraftlabs.sqs.util.AppArguments;

import javax.annotation.Nonnull;

public interface AppArgumentsValidationPolicy {
    void verify(@Nonnull AppArguments args) throws InvalidArgumentException;
}
