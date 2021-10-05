package com.epam.rd.controller;

import com.epam.rd.dto.GroupDTO;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.service.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"", "api/group"})
public class GroupRestController {
    @Autowired
    GroupServiceImpl groupServiceImpl;

    private static final String GROUP_ADDED = "Group added successfully!!";
    private static final String GROUP_UPDATED = "Group updated successfully!!";
    private static final String GROUP_DELETED = "Group deleted successfully!!";

    @GetMapping(value = "/viewGroups")
    public ResponseEntity<List<Group>> getGroupList() {
        List<Group> groupList = groupServiceImpl.findAllGroups();
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @PostMapping(value = "/addGroup")
    public ResponseEntity<Object> addGroup(@Valid @RequestBody GroupDTO groupDTO) {
        Group addedGroup = groupServiceImpl.addGroup(groupDTO);
        return new ResponseEntity<>(GROUP_ADDED + " " + addedGroup, HttpStatus.OK);
    }

    @GetMapping(value = "findRecordByGroup/{name}")
    public ResponseEntity<List<Record>> getRecordByGroup(@PathVariable String name)  {
        List<Record> recordList = groupServiceImpl.findAllRecordByGroupName(name);
        return new ResponseEntity<>(recordList, HttpStatus.OK);
    }

    @GetMapping(value = "showEditGroupForm/{name}")
    public ResponseEntity<Object> getEditGroupForm(@PathVariable String name, HttpSession session)  {
        Group fetchGroup = groupServiceImpl.findGroupByName(name);
        session.setAttribute("group_id", fetchGroup.getId());
        return new ResponseEntity<>(fetchGroup, HttpStatus.OK);
    }

    @PutMapping(value = "/updateGroup/{groupId}")
    public ResponseEntity<Object> updateGroup(@PathVariable int groupId, @RequestBody @Valid GroupDTO groupDTO) {
        groupDTO.setId(groupId);
        Group updatedGroup = groupServiceImpl.updateGroup(groupDTO);
        return new ResponseEntity<>(GROUP_UPDATED + " " + updatedGroup, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteGroup/{groupName}")
    public ResponseEntity<Object> deleteGroup(@PathVariable String groupName) {
        Group deletedGroup = groupServiceImpl.deleteGroup(groupName);
        return new ResponseEntity<>(GROUP_DELETED + " " + deletedGroup, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
