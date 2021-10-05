package com.epam.rd.service;

import com.epam.rd.converter.Convert;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.repository.GroupRepository;
import com.epam.rd.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordServiceImpl implements RecordService {
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    GroupRepository groupRepository;

    private Account account;

    @Override
    public Record addRecord(RecordDTO recordDTO) {
        Record record = Convert.convertToEntity(recordDTO);
        record.setAccount(account);
        recordRepository.findByUrlAndAccount(record.getUrl(), record.getAccount())
                .ifPresent(s -> {
                    throw new RecordAlreadyExistsException();
                });
        Group existGroup = groupRepository.findByNameAndAccount(recordDTO.getGroupName(), record.getAccount())
                .orElseThrow(NoGroupFoundForAccount::new);
        record.setGroup(existGroup);
        return recordRepository.save(record);
    }

    @Override
    public Record findRecordByUrl(String url) {
        return recordRepository.findByUrlAndAccount(url, account)
                .orElseThrow(NoRecordFoundForAccountBasedOnUrl::new);
    }

    @Override
    public List<Record> findAllRecords() {
        List<Record> recordList = recordRepository.findByAccount(account);
        if (recordList.isEmpty()) {
            throw new NoRecordFoundForAccount();
        }
        return recordList;
    }

    @Override
    public Record updateRecord(RecordDTO recordDTO) {
        Record record = Convert.convertToEntity(recordDTO);
        record.setAccount(account);
        Record foundRecord = recordRepository.findByUrlAndAccount(record.getUrl(), account)
                .orElseThrow(NoRecordFoundForAccountBasedOnUrl::new);
        foundRecord.setUserName(record.getUserName());
        foundRecord.setPassword(record.getPassword());
        foundRecord.setNotes(record.getNotes());
        return recordRepository.save(foundRecord);
    }

    @Override
    public Record deleteRecord(int id) {
        Record fetchRecord = recordRepository.findByIdAndAccount(id, account)
                .orElseThrow(NoRecordFoundForAccountBasedOnId::new);
        recordRepository.deleteByIdAndAccount(id, account);
        return fetchRecord;
    }

    @Override
    public void setAccount(LoginDTO loginDTO) {
        this.account = Convert.convertToEntity(loginDTO);
    }

    @Override
    public Account getAccount() {
        return this.account;
    }
}
