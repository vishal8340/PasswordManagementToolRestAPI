package com.epam.rd.exception;

public class NoRecordFoundForAccountBasedOnId extends Exception{
    public NoRecordFoundForAccountBasedOnId(){
        super("Oops! No Record Found For Id.");
    }
}
