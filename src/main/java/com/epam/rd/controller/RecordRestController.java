package com.epam.rd.controller;

import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.service.GroupServiceImpl;
import com.epam.rd.service.RecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"", "api/record"})
public class RecordRestController {
    @Autowired
    RecordServiceImpl recordServiceImpl;
    @Autowired
    GroupServiceImpl groupServiceImpl;

    private static final String RECORD_UPDATED = "Record updated successfully!!";
    private static final String RECORD_ADDED = "Record added successfully!!";
    private static final String RECORD_DELETED = "Record deleted successfully!!";

    @GetMapping(value = "/viewRecords")
    public ResponseEntity<List<Record>> getUserRecordList() throws NoRecordFoundForAccount {
        List<Record> recordList = recordServiceImpl.findAllRecords();
        return new ResponseEntity<>(recordList, HttpStatus.OK);
    }

    @GetMapping(value = "showEditRecordForm/{url}")
    public ResponseEntity<Object> getEditRecordForm(@PathVariable String url) throws NoRecordFoundForAccountBasedOnUrl {
        Record fetchRecord = recordServiceImpl.findRecordBasedOnUrl(url);
        return new ResponseEntity<>(fetchRecord, HttpStatus.OK);
    }

    @PutMapping(value = "/updateRecord")
    public ResponseEntity<Object> updateRecord(@RequestBody @Valid RecordDTO recordDTO) throws UnableToUpdateRecord, NoRecordFoundForAccountBasedOnUrl {
        Record updatedRecord = recordServiceImpl.updateRecord(recordDTO);
        return new ResponseEntity<>(RECORD_UPDATED + " " + updatedRecord, HttpStatus.OK);
    }

    @GetMapping(value = "showNewRecordForm")
    public ResponseEntity<List<String>> getNewRecordForm() throws NoGroupFoundForAccount {
        return new ResponseEntity<>(getGroupNameList(), HttpStatus.OK);
    }

    private List<String> getGroupNameList() throws NoGroupFoundForAccount {
        List<Group> groupList = groupServiceImpl.findAllGroups();
        return groupList.stream().map(Group::getName).collect(Collectors.toList());
    }

    @PostMapping(value = "/addRecord")
    public ResponseEntity<Object> addRecord(@RequestBody @Valid RecordDTO recordDTO) throws RecordAlreadyExistsException, UnableToAddRecord {
        Record addedRecord = recordServiceImpl.addRecord(recordDTO);
        return new ResponseEntity<>(RECORD_ADDED + " " + addedRecord, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteRecord/{id}")
    public ResponseEntity<Object> deleteRecord(@PathVariable int id) throws NoRecordFoundForAccountBasedOnId {
        Record deletedRecord = recordServiceImpl.deleteRecord(id);
        return new ResponseEntity<>(RECORD_DELETED + " " + deletedRecord, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}