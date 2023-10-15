package com.example.ravtech.exceptions;

public class DocumentSaveException extends RuntimeException {
    public DocumentSaveException(String message) {
        super(message);
    }

    public DocumentSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}