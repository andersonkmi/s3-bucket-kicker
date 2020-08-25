package org.codecraftlabs.sqs.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.sqs.util.AppArguments;

import javax.annotation.Nonnull;

import java.util.Objects;

import static org.codecraftlabs.sqs.util.AppArguments.OPERATION_OPTION;
import static org.codecraftlabs.sqs.util.AppArguments.RECV_MESSAGE_OPERATION;
import static org.codecraftlabs.sqs.util.AppArguments.SEND_MESSAGE_OPERATION;

class OperationValidationPolicy implements AppArgumentsValidationPolicy {
    private static final Logger logger = LogManager.getLogger(OperationValidationPolicy.class);

    @Override
    public void verify(@Nonnull AppArguments args) throws InvalidArgumentException {
        logger.info("Applying policy");
        var operation = args.option(OPERATION_OPTION);

        if (!Objects.equals(operation, SEND_MESSAGE_OPERATION) && !Objects.equals(operation, RECV_MESSAGE_OPERATION)) {
            throw new InvalidArgumentException("Invalid operation argument");
        }
    }
}
