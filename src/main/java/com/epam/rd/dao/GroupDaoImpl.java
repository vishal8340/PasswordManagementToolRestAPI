package com.epam.rd.dao;

import com.epam.rd.converter.Convert;
import com.epam.rd.dto.GroupDTO;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;
import com.epam.rd.repository.GroupRepository;
import com.epam.rd.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GroupDaoImpl implements GroupDao {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    RecordRepository recordRepository;
    private Account account;

    @Override
    public Group addGroup(GroupDTO groupDTO) throws UnableToAddGroup, GroupAlreadyExistsException {
        Group group = Convert.convertToEntity(groupDTO);
        group.setAccount(account);
        Group existGroup = groupRepository.findByNameAndAccount(group.getName(), group.getAccount());
        if (existGroup != null) {
            throw new GroupAlreadyExistsException();
        }
        Group addedGroup = groupRepository.save(group);
        if (addedGroup == null) {
            throw new UnableToAddGroup();
        }
        return addedGroup;
    }

    @Override
    public List<Record> findAllRecordByGroupName(String name) throws NoRecordFoundForGroup, NoGroupFoundForAccount {
        Group existGroup = findGroupByName(name);
        List<Record> recordList = recordRepository.findByGroupAndAccount(existGroup, account);
        if (recordList.isEmpty()) {
            throw new NoRecordFoundForGroup();
        }
        return recordList;
    }

    @Override
    public Group updateGroup(GroupDTO groupDTO) throws UnableToUpdateGroup, NoGroupFoundForAccount {
        Group group = Convert.convertToEntity(groupDTO);
        group.setAccount(account);
        Optional<Group> fetchGroup = groupRepository.findById(group.getId());
        if (fetchGroup.isEmpty()) {
            throw new NoGroupFoundForAccount();
        }
        Group existGroup = fetchGroup.get();
        existGroup.setName(group.getName());
        existGroup.setDescription(group.getDescription());
        Group updatedGroup = groupRepository.save(existGroup);
        if (updatedGroup == null) {
            throw new UnableToUpdateGroup();
        }
        return updatedGroup;
    }

    @Override
    public List<Group> findAllGroups() throws NoGroupFoundForAccount {
        List<Group> groupList = groupRepository.findByAccount(account);
        if (groupList.isEmpty()) {
            throw new NoGroupFoundForAccount();
        }
        return groupList;
    }

    @Override
    public Group deleteGroup(String name) throws NoGroupFoundForAccount, GroupShouldNotContainsRecords, UnableToDeleteGroup {
        Group existGroup = groupRepository.findByNameAndAccount(name, account);
        if (existGroup == null) {
            throw new NoGroupFoundForAccount();
        }
        List<Record> recordList = recordRepository.findByGroupAndAccount(existGroup, account);
        if (recordList.isEmpty()) {
            groupRepository.deleteById(existGroup.getId());
        } else {
            throw new GroupShouldNotContainsRecords();
        }
        return existGroup;
    }

    @Override
    public void setAccount(LoginDTO loginDTO) {
        this.account = Convert.convertToEntity(loginDTO);
    }

    @Override
    public Group findGroupByName(String name) throws NoGroupFoundForAccount {
        Group fetchGroup = groupRepository.findByNameAndAccount(name, account);
        if (fetchGroup == null) {
            throw new NoGroupFoundForAccount();
        }
        return fetchGroup;
    }

    @Override
    public Account getAccount() {
        return this.account;
    }
}
