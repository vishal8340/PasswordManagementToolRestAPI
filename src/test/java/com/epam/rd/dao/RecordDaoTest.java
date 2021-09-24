package com.epam.rd.dao;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.repository.GroupRepository;
import com.epam.rd.repository.RecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordDaoTest {
    @InjectMocks
    RecordDaoImpl recordDaoImpl;

    @Mock
    RecordRepository recordRepository;

    @Mock
    GroupRepository groupRepository;

    @Test
    void testAddRecordThrowExceptionWhenRecordAlreadyExists() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.existsByUrlAndAccount(any(), any())).thenReturn(true);
        Assertions.assertThrows(RecordAlreadyExistsException.class, () -> recordDaoImpl.addRecord(recordDTO));
    }

    @Test
    void testAddRecordThrowExceptionWhenUnableToAddRecord() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        Group existGroup = new Group();
        when(recordRepository.save(any())).thenReturn(null);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(existGroup);
        when(recordRepository.existsByUrlAndAccount(any(), any())).thenReturn(false);
        Assertions.assertThrows(UnableToAddRecord.class, () -> recordDaoImpl.addRecord(recordDTO));
    }

    @Test
    void testAddRecordDoesNotThrowExceptionWhileNewRecordInsertion() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        Record record = new Record();
        Group existGroup = new Group();
        when(recordRepository.save(any())).thenReturn(record);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(existGroup);
        when(recordRepository.existsByUrlAndAccount(any(), any())).thenReturn(false);
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.addRecord(recordDTO));
    }

    @Test
    void findRecordBasedOnUrlThrowExceptionWhenNoRecordFoundForProvidedUrl() {
        String url = "http://www.master.com";
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(null);
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnUrl.class, () -> recordDaoImpl.findRecordBasedOnUrl(url));
    }

    @Test
    void findRecordBasedOnUrlDoesNotThrowExceptionWhileRecordFoundForProvidedUrl() {
        String url = "http://www.master.com";
        Record record = new Record();
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(record);
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.findRecordBasedOnUrl(url));
    }

    @Test
    void findRecordBasedOnUrlReturnRecordWhileProvidedUrlExistsWithAccount() throws NoRecordFoundForAccountBasedOnUrl {
        String url = "http://www.master.com";
        Record record = new Record();
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(record);
        Assertions.assertEquals(record, recordDaoImpl.findRecordBasedOnUrl(url));
    }

    @Test
    void findAllRecordsThrowExceptionWhenNoRecordFoundForAccount() {
        when(recordRepository.findByAccount(any())).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NoRecordFoundForAccount.class, () -> recordDaoImpl.findAllRecords());
    }

    @Test
    void findAllRecordsDoesNotThrowExceptionWhileRecordFoundForAccount() {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "http://www.,master.com", "record data"));
        when(recordRepository.findByAccount(any())).thenReturn(recordList);
        Assertions.assertEquals(recordList, Assertions.assertDoesNotThrow(() -> recordDaoImpl.findAllRecords()));
    }

    @Test
    void testUpdateRecordThrowExceptionWhenNoRecordFoundForAccountBasedOnUrl() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(null);
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnUrl.class, () -> recordDaoImpl.updateRecord(recordDTO));
    }

    @Test
    void testUpdateRecordThrowExceptionWhenUnableToUpdateRecord() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        Record record = new Record();
        when(recordRepository.save(any())).thenReturn(null);
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(record);
        Assertions.assertThrows(UnableToUpdateRecord.class, () -> recordDaoImpl.updateRecord(recordDTO));
    }

    @Test
    void updateRecordDoesNotThrowExceptionWhileRecordFoundForAccountBasedOnUrl() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        Record record = new Record();
        when(recordRepository.save(any())).thenReturn(record);
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(record);
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.updateRecord(recordDTO));
    }

    @Test
    void deleteRecordThrowExceptionWhenNoRecordFoundForId() {
        int id = 12;
        when(recordRepository.findByIdAndAccount(any(), any())).thenReturn(null);
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnId.class, () -> recordDaoImpl.deleteRecord(id));
    }

    @Test
    void deleteRecordDoesNotThrowExceptionWhenRecordFoundForId() {
        int id = 12;
        Record record = new Record("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        doNothing().when(recordRepository).deleteByIdAndAccount(any(),any());
        when(recordRepository.findByIdAndAccount(any(),any())).thenReturn(record);
        Assertions.assertEquals(record, Assertions.assertDoesNotThrow(() -> recordDaoImpl.deleteRecord(id)));
    }

    @Test
    void testAccount() {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        recordDaoImpl.setAccount(loginDTO);
        Account account = new Account("KGR009517", "Vishal834019");
        Assertions.assertEquals(account.getUserName(), recordDaoImpl.getAccount().getUserName());
    }
}
