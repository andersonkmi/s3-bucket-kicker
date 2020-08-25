package org.codecraftlabs.sqs.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codecraftlabs.sqs.util.AppArguments;

import javax.annotation.Nonnull;

import static org.codecraftlabs.sqs.util.AppArguments.SQS_URL_OPTION;

class UrlValidationPolicy implements AppArgumentsValidationPolicy {
    private static final Logger logger = LogManager.getLogger(UrlValidationPolicy.class);

    @Override
    public void verify(@Nonnull AppArguments args) throws InvalidArgumentException {
        logger.info("Applying policy");
        var url = args.option(SQS_URL_OPTION);

        if (url.contains("https")) {
            logger.info(String.format("URL '%s' is valid", url));
        } else {
            throw new InvalidArgumentException(String.format("URL '%s' is invalid", url));
        }
    }
}
