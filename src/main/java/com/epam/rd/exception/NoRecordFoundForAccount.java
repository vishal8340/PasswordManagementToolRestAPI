package com.epam.rd.exception;

public class NoRecordFoundForAccount extends RuntimeException {
    public NoRecordFoundForAccount(){
        super("Oops! No Record Found!!");
    }
}
