package com.finalpos.POSsystem.Exception;

public class FailedException extends RuntimeException{
    public FailedException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailedException(String message) {
        super(message);
    }
}
