package com.epam.rd.service;

import com.epam.rd.dto.GroupDTO;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.GroupAlreadyExistsException;
import com.epam.rd.exception.GroupShouldNotContainsRecords;
import com.epam.rd.exception.NoGroupFoundForAccount;
import com.epam.rd.exception.NoRecordFoundForGroup;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @InjectMocks
    GroupServiceImpl groupDaoImpl;
    @Mock
    GroupRepository groupRepository;
    @Mock
    RecordRepository recordRepository;

    @Test
    void testAddGroupThrowExceptionWhenGroupAlreadyExists() {
        GroupDTO groupDTO = new GroupDTO("Google", "first group");
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertThrows(GroupAlreadyExistsException.class, () -> groupDaoImpl.addGroup(groupDTO));
    }

    @Test
    void testAddGroupDoesNotThrowExceptionWhileInsertingNewGroup() {
        GroupDTO groupDTO = new GroupDTO("Google", "first group");
        Group addedGroup = new Group();
        when(groupRepository.save(any())).thenReturn(addedGroup);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> groupDaoImpl.addGroup(groupDTO));
    }

    @Test
    void testFindAllRecordByGroupNameThrowExceptionWhenNoRecordExistForSearchedGroupName() {
        when(recordRepository.findByGroupAndAccount(any(), any())).thenReturn(Collections.emptyList());
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertThrows(NoRecordFoundForGroup.class, () -> groupDaoImpl.findAllRecordByGroupName("Google"));
    }

    @Test
    void testFindAllRecordByGroupNameDoesNotThrowExceptionWhileRecordsFound() {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "htt://www.master.com", "data123"));
        when(recordRepository.findByGroupAndAccount(any(), any())).thenReturn(recordList);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertEquals(recordList, Assertions.assertDoesNotThrow(() -> groupDaoImpl.findAllRecordByGroupName("Google")));
    }

    @Test
    void testUpdateGroupThrowExceptionWhenNoGroupExist() {
        GroupDTO groupDTO = new GroupDTO("Google", "first group");
        when(groupRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoGroupFoundForAccount.class, () -> groupDaoImpl.updateGroup(groupDTO));
    }

    @Test
    void testUpdateGroupThrowExceptionWhenGroupAlreadyExists() {
        GroupDTO groupDTO = new GroupDTO("Google", "first group");
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        when(groupRepository.findById(any())).thenReturn(Optional.of(new Group()));
        Assertions.assertThrows(GroupAlreadyExistsException.class, () -> groupDaoImpl.updateGroup(groupDTO));
    }

    @Test
    void testUpdateGroupDoesNotThrowExceptionWhileUpdatingGroupSuccessfully() {
        GroupDTO groupDTO = new GroupDTO("Google", "first group");
        Group fetchGroup = new Group();
        when(groupRepository.save(any())).thenReturn(fetchGroup);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.empty());
        when(groupRepository.findById(any())).thenReturn(Optional.of(new Group()));
        Assertions.assertDoesNotThrow(() -> groupDaoImpl.updateGroup(groupDTO));
    }

    @Test
    void testFindAllGroupThrowExceptionWhenNoGroupExist() {
        when(groupRepository.findByAccount(any())).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NoGroupFoundForAccount.class, () -> groupDaoImpl.findAllGroups());
    }

    @Test
    void testFindAllGroupDoesNotThrowExceptionWhileGroupExist() {
        List<Group> groupList = List.of(new Group(1, "Google", "data group"));
        when(groupRepository.findByAccount(any())).thenReturn(groupList);
        Assertions.assertDoesNotThrow(() -> groupDaoImpl.findAllGroups());
    }

    @Test
    void testFindGroupByNameThrowExceptionWhenNoGroupExist() {
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoGroupFoundForAccount.class, () -> groupDaoImpl.findGroupByName("Google"));
    }

    @Test
    void testFindGroupByNameDoesNotThrowExceptionWhileGroupExist() {
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertDoesNotThrow(() -> groupDaoImpl.findGroupByName("Google"));
    }

    @Test
    void testDeleteGroupThrowExceptionWhenGroupHasRecords() {
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(NoGroupFoundForAccount.class, () -> groupDaoImpl.deleteGroup("Google"));
    }

    @Test
    void testDeleteGroupThrowExceptionWhenNoGroupExistsWithRecords() {
        List<Record> recordList = List.of(new Record("KGR009517", "Vishal834019", "http://www.google.com", "data group"));
        when(recordRepository.findByGroupAndAccount(any(), any())).thenReturn(recordList);
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertThrows(GroupShouldNotContainsRecords.class, () -> groupDaoImpl.deleteGroup("Google"));
    }

    @Test
    void testDeleteGroupDoesNotThrowExceptionWhileDeletingGroupWithoutRecords() {
        when(recordRepository.findByGroupAndAccount(any(), any())).thenReturn(Collections.emptyList());
        when(groupRepository.findByNameAndAccount(any(), any())).thenReturn(Optional.of(new Group()));
        Assertions.assertDoesNotThrow(() -> groupDaoImpl.deleteGroup("Google"));
    }

    @Test
    void testAccount() {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        groupDaoImpl.setAccount(loginDTO);
        Account account = new Account("KGR009517", "Vishal834019");
        Assertions.assertEquals(account.getUserName(), groupDaoImpl.getAccount().getUserName());
    }
}
