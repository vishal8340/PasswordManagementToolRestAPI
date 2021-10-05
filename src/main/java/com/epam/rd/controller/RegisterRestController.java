package com.epam.rd.controller;

import com.epam.rd.dto.RegisterDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterRestController {

    @Autowired
    AccountServiceImpl accountServiceImpl;

    private static final String ACCOUNT_ADDED = "User Registered Successfully!";


    @PostMapping(value = "/api/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDTO registerDTO) {
        Account registerAccount = accountServiceImpl.registerAccount(registerDTO);
        return new ResponseEntity<>(ACCOUNT_ADDED + " " + registerAccount, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
