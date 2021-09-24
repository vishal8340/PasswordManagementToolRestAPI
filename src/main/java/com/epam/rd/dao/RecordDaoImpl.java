package com.epam.rd.dao;

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
public class RecordDaoImpl implements RecordDao {
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    GroupRepository groupRepository;

    private Account account;

    @Override
    public Record addRecord(RecordDTO recordDTO) throws UnableToAddRecord, RecordAlreadyExistsException {
        Record record = Convert.convertToEntity(recordDTO);
        record.setAccount(account);
        boolean existRecord = recordRepository.existsByUrlAndAccount(record.getUrl(), account);
        if (existRecord) {
            throw new RecordAlreadyExistsException();
        }
        Group existGroup = groupRepository.findByNameAndAccount(recordDTO.getGroupName(), account);
        record.setGroup(existGroup);
        Record addedRecord = recordRepository.save(record);
        if (addedRecord == null) {
            throw new UnableToAddRecord();
        }
        return addedRecord;
    }

    @Override
    public Record findRecordBasedOnUrl(String url) throws NoRecordFoundForAccountBasedOnUrl {
        Record fetchRecord = recordRepository.findByUrlAndAccount(url, account);
        if (fetchRecord == null) {
            throw new NoRecordFoundForAccountBasedOnUrl();
        } else {
            return fetchRecord;
        }
    }

    @Override
    public List<Record> findAllRecords() throws NoRecordFoundForAccount {
        List<Record> recordList = recordRepository.findByAccount(account);
        if (recordList.isEmpty()) {
            throw new NoRecordFoundForAccount();
        }
        return recordList;
    }

    @Override
    public Record updateRecord(RecordDTO recordDTO) throws UnableToUpdateRecord, NoRecordFoundForAccountBasedOnUrl {
        Record record = Convert.convertToEntity(recordDTO);
        record.setAccount(account);
        Record foundRecord = recordRepository.findByUrlAndAccount(record.getUrl(), account);
        if (foundRecord == null) {
            throw new NoRecordFoundForAccountBasedOnUrl();
        }
        foundRecord.setUserName(record.getUserName());
        foundRecord.setPassword(record.getPassword());
        foundRecord.setNotes(record.getNotes());
        Record updatedRecord = recordRepository.save(foundRecord);
        if (updatedRecord == null) {
            throw new UnableToUpdateRecord();
        }
        return updatedRecord;
    }

    @Override
    public Record deleteRecord(int id) throws NoRecordFoundForAccountBasedOnId {
        Record fetchRecord = recordRepository.findByIdAndAccount(id, account);
        if (fetchRecord == null) {
            throw new NoRecordFoundForAccountBasedOnId();
        } else {
            recordRepository.deleteByIdAndAccount(id, account);
            return fetchRecord;
        }
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
