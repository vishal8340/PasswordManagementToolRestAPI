package com.epam.rd.service;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RecordDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.NoRecordFoundForAccount;
import com.epam.rd.exception.NoRecordFoundForAccountBasedOnId;
import com.epam.rd.exception.NoRecordFoundForAccountBasedOnUrl;
import com.epam.rd.exception.RecordAlreadyExistsException;
import com.epam.rd.repository.GroupRepository;
import com.epam.rd.repository.RecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {
    @InjectMocks
    RecordServiceImpl recordDaoImpl;

    @Mock
    RecordRepository recordRepository;

    @Mock
    GroupRepository groupRepository;

    @Test
    void addRecordThrowExceptionWhenRecordAlreadyExists() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.of(new Record()));
        Assertions.assertThrows(RecordAlreadyExistsException.class, () -> recordDaoImpl.addRecord(recordDTO));
    }

    @Test
    void addRecordDoesNotThrowExceptionWhileAddingNewRecord() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.save(any())).thenReturn(new Record());
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.addRecord(recordDTO));
    }

    @Test
    void findRecordByUrlThrowExceptionWhenNoRecordFoundForProvidedUrl() {
        String url = "http://www.master.com";
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnUrl.class, () -> recordDaoImpl.findRecordByUrl(url));
    }

    @Test
    void findRecordByUrlDoesNotThrowExceptionWhileRecordFoundForProvidedUrl() {
        String url = "http://www.master.com";
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.of(new Record()));
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.findRecordByUrl(url));
    }

    @Test
    void findAllRecordsThrowExceptionWhenNoRecordFound() {
        when(recordRepository.findByAccount(any())).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NoRecordFoundForAccount.class, () -> recordDaoImpl.findAllRecords());
    }

    @Test
    void findAllRecordsDoesNotThrowExceptionWhileRecordFound() {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "http://www.,master.com", "record data"));
        when(recordRepository.findByAccount(any())).thenReturn(recordList);
        Assertions.assertEquals(recordList, Assertions.assertDoesNotThrow(() -> recordDaoImpl.findAllRecords()));
    }

    @Test
    void updateRecordThrowExceptionWhenNoRecordFoundForAccountByUrl() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnUrl.class, () -> recordDaoImpl.updateRecord(recordDTO));
    }

    @Test
    void updateRecordDoesNotThrowExceptionWhileRecordFoundForAccountBasedOnUrl() {
        RecordDTO recordDTO = new RecordDTO("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        when(recordRepository.save(any())).thenReturn(new Record());
        when(recordRepository.findByUrlAndAccount(any(), any())).thenReturn(Optional.of(new Record()));
        Assertions.assertDoesNotThrow(() -> recordDaoImpl.updateRecord(recordDTO));
    }

    @Test
    void deleteRecordThrowExceptionWhenNoRecordFoundById() {
        when(recordRepository.findByIdAndAccount(anyInt(), any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoRecordFoundForAccountBasedOnId.class, () -> recordDaoImpl.deleteRecord(12));
    }

    @Test
    void deleteRecordDoesNotThrowExceptionWhenRecordFoundById() {
        int id = 12;
        Record record = new Record("KGR009517", "Vishi834019", "http://www.epam.com", "first record");
        doNothing().when(recordRepository).deleteByIdAndAccount(anyInt(),any());
        when(recordRepository.findByIdAndAccount(anyInt(),any())).thenReturn(Optional.of(record));
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
