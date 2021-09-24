package com.epam.rd.controller;

import com.epam.rd.dto.GroupDTO;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.service.GroupServiceImpl;
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
import static org.mockito.Mockito.when;

@WebMvcTest(GroupRestController.class)
public class GroupRestControllerTest {
    private static final String GROUP_ADDED = "Group added successfully!!";
    private static final String GROUP_UPDATED = "Group updated successfully!!";
    private static final String GROUP_DELETED = "Group deleted successfully!!";

    @Autowired
    MockMvc mvc;

    @MockBean
    GroupServiceImpl groupServiceImpl;

    @Test
    public void testFindRecordByGroupNameReturnListOfGroup() throws Exception {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "http://www.master.com", "group1"));
        when(groupServiceImpl.findAllRecordByGroupName(any())).thenReturn(recordList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/group/findRecordByGroup/EpamPvt");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        ObjectMapper mapper = new ObjectMapper();
        List<Record> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(actual.toString(), recordList.toString());
    }

    @Test
    public void testGetEditGroupFormReturnGroup() throws Exception {
        Group group = new Group();
        when(groupServiceImpl.findGroupByName(any())).thenReturn(group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/group/showEditGroupForm/EpamPvt");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    public void testAddGroupThrowExceptionWhenGroupAlreadyExistsException() throws Exception {
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.addGroup(any())).thenThrow(new GroupAlreadyExistsException());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/group/addGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Group already exists!!", content);
    }

    @Test
    public void testAddGroupThrowExceptionWhenUnableToAddGroup() throws Exception {
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.addGroup(any())).thenThrow(new UnableToAddGroup());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/group/addGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Unable to Add Group!!", content);
    }

    @Test
    public void testAddGroupDoesNotThrowExceptionWhileAddingGroupSuccessfully() throws Exception {
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        Group addedGroup = new Group();
        when(groupServiceImpl.addGroup(any())).thenReturn(addedGroup);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/group/addGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(GROUP_ADDED + " " + addedGroup, content);
    }

    @Test
    public void testGetGroupListThrowExceptionWhenNoGroupFoundForAccount() throws Exception {
        when(groupServiceImpl.findAllGroups()).thenThrow(new NoGroupFoundForAccount());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/group/viewGroups");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No group found!!", content);
    }

    @Test
    public void testGetGroupListDoesNotThrowExceptionWhileGroupExists() throws Exception {
        List<Group> groupList = Arrays.asList(new Group("Google", "adding description of google"),
                new Group("Bsnl", "data"));
        when(groupServiceImpl.findAllGroups()).thenReturn(groupList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/group/viewGroups");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        ObjectMapper mapper = new ObjectMapper();
        List<Group> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertEquals(actual.toString(), groupList.toString());
    }

    @Test
    public void testUpdateGroupThrowExceptionWhenNoGroupFoundForAccount() throws Exception {
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.updateGroup(any())).thenThrow(new NoGroupFoundForAccount());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/group/updateGroup/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No group found!!", content);
    }

    @Test
    public void testUpdateGroupThrowExceptionWhenUnableToUpdateGroup() throws Exception {
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.updateGroup(any())).thenThrow(new UnableToUpdateGroup());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/group/updateGroup/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! unable to update group!!", content);
    }

    @Test
    public void testUpdateGroupDoesNotThrowExceptionWhileUpdatingGroupSuccessfully() throws Exception {
        Group updatedGroup = new Group();
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.updateGroup(any())).thenReturn(updatedGroup);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/group/updateGroup/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(GROUP_UPDATED + " " + updatedGroup, content);
    }

    @Test
    public void testDeleteGroupThrowExceptionWhenGroupShouldNotContainsRecords() throws Exception {
        when(groupServiceImpl.deleteGroup(any())).thenThrow(new GroupShouldNotContainsRecords());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/group/deleteGroup/Google");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Group should be empty before deletion!!", content);
    }

    @Test
    public void testDeleteGroupThrowExceptionWhenNoGroupFoundForAccount() throws Exception {
        when(groupServiceImpl.deleteGroup(any())).thenThrow(new NoGroupFoundForAccount());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/group/deleteGroup/Google");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! No group found!!", content);
    }

    @Test
    public void testDeleteGroupThrowExceptionWhenUnableToDeleteGroup() throws Exception {
        when(groupServiceImpl.deleteGroup(any())).thenThrow(new UnableToDeleteGroup());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/group/deleteGroup/Google");

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(406, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Oops! Unable to delete group.", content);
    }

    @Test
    public void testDeleteGroupDoesNotThrowExceptionWhileDeletingSuccessfully() throws Exception {
        Group deletedGroup = new Group();
        GroupDTO groupDTO = new GroupDTO("Google", "storing google based accounts");
        when(groupServiceImpl.deleteGroup(any())).thenReturn(deletedGroup);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/group/deleteGroup/Google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(groupDTO));

        MvcResult mvcResult = mvc.perform(requestBuilder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(GROUP_DELETED + " " + deletedGroup, content);
    }
}
