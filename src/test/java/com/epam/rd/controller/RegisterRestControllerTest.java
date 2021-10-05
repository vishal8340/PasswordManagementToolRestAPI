package com.epam.rd.controller;

import com.epam.rd.dto.RegisterDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.exception.AccountAlreadyExistsException;
import com.epam.rd.service.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(RegisterRestController.class)
public class RegisterRestControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testRegisterUserThrowExceptionWhenAccountAlreadyExists() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("Vishal Kumar", "KGR009517", "Vishi834019");
        when(accountServiceImpl.registerAccount(any())).thenThrow(new AccountAlreadyExistsException());
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Account already exists!!", content);
    }

    @Test
    public void testRegisterUserDoesNotThrowExceptionWhileRegisteringSuccessfully() throws Exception {
        final String ACCOUNT_ADDED = "User Registered Successfully!";
        RegisterDTO registerDTO = new RegisterDTO("Vishal Kumar", "KGR009517", "Vishi834019");
        Account registeredAccount = new Account();
        when(accountServiceImpl.registerAccount(any())).thenReturn(registeredAccount);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(ACCOUNT_ADDED + " " + registeredAccount, content);
    }
}
