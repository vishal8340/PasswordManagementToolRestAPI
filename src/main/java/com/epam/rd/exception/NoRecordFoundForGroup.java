package com.epam.rd.exception;

public class NoRecordFoundForGroup extends RuntimeException {
    public NoRecordFoundForGroup() {
        super("Oops! No Record found for group");
    }
}
