package com.epam.rd.controller;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.exception.AccountDoesNotExistException;
import com.epam.rd.exception.WrongPasswordException;
import com.epam.rd.service.AccountServiceImpl;
import com.epam.rd.service.GroupServiceImpl;
import com.epam.rd.service.RecordServiceImpl;
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

@WebMvcTest(LoginRestController.class)
public class LoginRestControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    AccountServiceImpl accountServiceImpl;
    @MockBean
    GroupServiceImpl groupServiceImpl;
    @MockBean
    RecordServiceImpl recordServiceImpl;

    @Test
    public void testLoginUserThrowExceptionWhenAccountDoesNotExist() throws Exception {
        LoginDTO loginDTO = new LoginDTO("KGR009517","Vishi834019");
        when(accountServiceImpl.validateLogin(any())).thenThrow(new AccountDoesNotExistException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No Account Found!!", content);
    }

    @Test
    public void testLoginUserThrowExceptionWhenWrongPasswordEntered() throws Exception {
        LoginDTO loginDTO = new LoginDTO("KGR009517","Vishi834019");
        when(accountServiceImpl.validateLogin(any())).thenThrow(new WrongPasswordException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Password does not matched!!", content);
    }

    @Test
    public void testLoginUserDoesNotThrowExceptionWithValidCredentials() throws Exception {
        String accountName = "Vishal";
        LoginDTO loginDTO = new LoginDTO("KGR009517","Vishi834019");
        when(accountServiceImpl.validateLogin(any())).thenReturn(accountName);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(accountName, content);
    }

}