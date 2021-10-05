package com.epam.rd.controller;


import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.service.GroupServiceImpl;
import com.epam.rd.service.RecordServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(RecordRestController.class)
public class RecordRestControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    RecordServiceImpl recordServiceImpl;
    @MockBean
    GroupServiceImpl groupServiceImpl;

    @Test
    public void testShowEditRecordForm() throws Exception {
        Record record = new Record("KGR009517", "Vishal834019", "http://www.master.com", "Record1");
        when(recordServiceImpl.findRecordByUrl(any())).thenReturn(record);

        String url = "www.master.com";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/record/showEditRecordForm/" + url);

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    public void testShowNewRecordForm() throws Exception {
        List<Group> groupList = Arrays.asList(new Group("Google", "group1"),
                new Group("EpamPvt", "group2"));
        when(groupServiceImpl.findAllGroups()).thenReturn(groupList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/record/showNewRecordForm");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    public void testAddRecordThrowExceptionWhenRecordAlreadyExistsException() throws Exception {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishal834019", "http://www.google.com", "records");
        when(recordServiceImpl.addRecord(any())).thenThrow(new RecordAlreadyExistsException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/record/addRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(recordDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Record already exists for url!!", content);
    }

    @Test
    public void testAddRecordDoesNotThrowExceptionWhileAddingNewRecord() throws Exception {
        final String RECORD_ADDED = "Record added successfully!!";
        Record addedRecord = new Record();
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishal834019", "http://www.google.com", "records");
        when(recordServiceImpl.addRecord(any())).thenReturn(addedRecord);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/record/addRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(recordDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(RECORD_ADDED + " " + addedRecord, content);
    }

    @Test
    public void testUpdateRecordThrowExceptionWhenNoRecordFoundForAccountBasedOnUrl() throws Exception {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishal834019", "http://www.google.com", "records");
        when(recordServiceImpl.updateRecord(any())).thenThrow(new NoRecordFoundForAccountBasedOnUrl());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/record/updateRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(recordDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No Record Found For Input Url!!", content);
    }

    @Test
    public void testUpdateRecordDoesNotThrowExceptionWhileUpdatingSuccessfully() throws Exception {
        final String RECORD_UPDATED = "Record updated successfully!!";
        Record updatedRecord = new Record();
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishal834019", "http://www.google.com", "records");
        when(recordServiceImpl.updateRecord(any())).thenReturn(updatedRecord);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/record/updateRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(recordDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(RECORD_UPDATED + " " + updatedRecord, content);
    }

    @Test
    public void testViewRecordThrowExceptionWhenNoRecordFoundForAccount() throws Exception {
        when(recordServiceImpl.findAllRecords()).thenThrow(new NoRecordFoundForAccount());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/record/viewRecords");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No Record Found!!", content);

    }

    @Test
    public void testViewRecordReturnListOfRecordWhileRecordFound() throws Exception {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "http://www.google.com", "records"));
        when(recordServiceImpl.findAllRecords()).thenReturn(recordList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/record/viewRecords");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        ObjectMapper mapper = new ObjectMapper();
        List<Record> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(actual.toString(), recordList.toString());
    }

    @Test
    public void testDeleteRecordThrowExceptionWhenNoRecordFoundForAccountBasedOnId() throws Exception {
        when(recordServiceImpl.deleteRecord(anyInt())).thenThrow(new NoRecordFoundForAccountBasedOnId());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/record/deleteRecord/2");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No Record Found For Id.", content);
    }

    @Test
    public void testDeleteRecordDoesNotThrowExceptionWhileDeletingSuccessfully() throws Exception {
        final String RECORD_DELETED = "Record deleted successfully!!";
        Record deletedRecord = new Record();
        when(recordServiceImpl.deleteRecord(anyInt())).thenReturn(deletedRecord);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/record/deleteRecord/2");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(RECORD_DELETED + " " + deletedRecord, content);
    }

}
