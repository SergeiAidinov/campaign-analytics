package com.gmail.aydinov.sergey.campaign_analytics.exception;

public class DataNotReadyException extends RuntimeException {

    public DataNotReadyException(String message) {
        super(message);
    }

    public DataNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }
}
