package org.codecraftlabs.sqs.validator;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
