package com.epam.rd.exception;

public class AccountDoesNotExistException extends RuntimeException {
    public AccountDoesNotExistException() {
        super("Oops! No Account Found!!");
    }
}
