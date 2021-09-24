package com.epam.rd.service;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;

import java.util.List;

public interface RecordService {
    Record addRecord(RecordDTO recordDTO) throws UnableToAddRecord, RecordAlreadyExistsException;

    Record findRecordBasedOnUrl(String url) throws NoRecordFoundForAccountBasedOnUrl;

    List<Record> findAllRecords() throws NoRecordFoundForAccount;

    Record updateRecord(RecordDTO recordDTO) throws UnableToUpdateRecord, NoRecordFoundForAccountBasedOnUrl;

    Record deleteRecord(int id) throws NoRecordFoundForAccountBasedOnId;

    void setAccount(LoginDTO loginDTO);

    Account getAccount();
}
