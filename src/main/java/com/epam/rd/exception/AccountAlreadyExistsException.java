package com.epam.rd.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException() {
        super("Oops! Account already exists!!");
    }
}
