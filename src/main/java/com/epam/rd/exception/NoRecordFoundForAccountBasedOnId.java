package com.epam.rd.exception;

public class NoRecordFoundForAccountBasedOnId extends RuntimeException {
    public NoRecordFoundForAccountBasedOnId() {
        super("Oops! No Record Found For Id.");
    }
}
