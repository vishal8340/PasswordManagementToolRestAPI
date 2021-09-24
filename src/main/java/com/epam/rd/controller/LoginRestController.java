package com.epam.rd.controller;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.exception.AccountDoesNotExistException;
import com.epam.rd.exception.WrongPasswordException;
import com.epam.rd.service.AccountServiceImpl;
import com.epam.rd.service.GroupServiceImpl;
import com.epam.rd.service.RecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class LoginRestController {
    @Autowired
    AccountServiceImpl accountServiceImpl;

    @Autowired
    GroupServiceImpl groupServiceImpl;

    @Autowired
    RecordServiceImpl recordServiceImpl;

    @PostMapping(value = "/api/loginUser")
    public ResponseEntity<String> loginUser(@RequestBody @Valid LoginDTO loginDTO, HttpSession session) throws AccountDoesNotExistException, WrongPasswordException {
        String accountName = accountServiceImpl.validateLogin(loginDTO);
        groupServiceImpl.setAccount(loginDTO);
        recordServiceImpl.setAccount(loginDTO);
        session.setAttribute("accountName", accountName);
        session.setAttribute("account", loginDTO);
        return new ResponseEntity<>(accountName, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
