package org.codecraftlabs.sqs.util;

import java.util.Map;

public class AppArguments {
    public static final String SQS_URL_OPTION = "sqsUrl";
    public static final String INTERVAL_SECONDS_OPTION = "intervalSeconds";
    public static final String OPERATION_OPTION = "operation";
    public static final String SEND_MESSAGE_OPERATION = "send";
    public static final String RECV_MESSAGE_OPERATION = "receive";

    private Map<String, String> arguments;

    public AppArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public String option(String key) {
        return arguments.getOrDefault(key, "");
    }
}
