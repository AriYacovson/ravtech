package com.example.ravtech.exceptions;

public class DocumentDeleteException extends RuntimeException {
    public DocumentDeleteException(String message) {
        super(message);
    }

    public DocumentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
