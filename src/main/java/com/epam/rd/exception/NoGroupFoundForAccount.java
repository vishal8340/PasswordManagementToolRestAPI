package com.epam.rd.exception;

public class NoGroupFoundForAccount extends RuntimeException{
    public NoGroupFoundForAccount(){
        super("Oops! No group found!!");
    }
}
