package com.ncu.springboot.mvc.exception;

public class OwnException extends RuntimeException {
    public OwnException() {
    }

    public OwnException(String message) {
        super(message);
    }

    public OwnException(String message, Throwable cause) {
        super(message, cause);
    }

    public OwnException(Throwable cause) {
        super(cause);
    }

    public OwnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
