package com.example.ravtech.exceptions;

public class DocumentUpdateException extends RuntimeException {
    public DocumentUpdateException(String message) {
        super(message);
    }

    public DocumentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
