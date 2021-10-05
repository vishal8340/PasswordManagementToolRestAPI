package com.epam.rd.exception;

public class GroupShouldNotContainsRecords extends RuntimeException {
    public GroupShouldNotContainsRecords() {
        super("Oops! Group should be empty before deletion!!");
    }
}
