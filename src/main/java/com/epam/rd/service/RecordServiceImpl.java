package com.epam.rd.service;

import com.epam.rd.dao.RecordDaoImpl;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    RecordDaoImpl recordDaoImpl;

    @Override
    public Record addRecord(RecordDTO recordDTO) throws UnableToAddRecord, RecordAlreadyExistsException {
        return recordDaoImpl.addRecord(recordDTO);
    }

    @Override
    public Record findRecordBasedOnUrl(String url) throws NoRecordFoundForAccountBasedOnUrl {
        return recordDaoImpl.findRecordBasedOnUrl(url);
    }

    @Override
    public List<Record> findAllRecords() throws NoRecordFoundForAccount {
        return recordDaoImpl.findAllRecords();
    }

    @Override
    public Record updateRecord(RecordDTO recordDTO) throws UnableToUpdateRecord, NoRecordFoundForAccountBasedOnUrl {
        return recordDaoImpl.updateRecord(recordDTO);
    }

    @Override
    public Record deleteRecord(int id) throws NoRecordFoundForAccountBasedOnId {
        return recordDaoImpl.deleteRecord(id);
    }

    @Override
    public void setAccount(LoginDTO loginDTO) {
        recordDaoImpl.setAccount(loginDTO);
    }

    @Override
    public Account getAccount() {
        return recordDaoImpl.getAccount();
    }
}
