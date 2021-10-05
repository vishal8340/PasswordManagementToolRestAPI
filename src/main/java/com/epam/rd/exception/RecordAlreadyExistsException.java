package com.epam.rd.exception;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException() {
        super("Oops! Record already exists for url!!");
    }
}
