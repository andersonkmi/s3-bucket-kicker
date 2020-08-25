package org.codecraftlabs.sqs.util;

public class CommandLineException extends Exception {
    public CommandLineException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
