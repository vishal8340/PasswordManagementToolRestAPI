package com.epam.rd.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Oops! Password does not matched!!");
    }
}
