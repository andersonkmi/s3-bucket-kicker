package org.codecraftlabs.kikker.validator;

import org.codecraftlabs.kikker.util.CommandLineArguments;

import javax.annotation.Nonnull;

public interface AppArgumentsValidationPolicy {
    void verify(@Nonnull CommandLineArguments args) throws InvalidArgumentException;
}
