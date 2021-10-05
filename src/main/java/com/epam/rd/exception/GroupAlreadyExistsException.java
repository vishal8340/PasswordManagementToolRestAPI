package com.epam.rd.exception;

public class GroupAlreadyExistsException extends RuntimeException {
    public GroupAlreadyExistsException(){
        super("Oops! Group already exists!!");
    }
}
