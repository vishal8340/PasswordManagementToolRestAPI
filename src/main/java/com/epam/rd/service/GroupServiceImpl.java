package com.epam.rd.service;

import com.epam.rd.converter.Convert;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupServiceImpl implements GroupService {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    RecordRepository recordRepository;
    private Account account;

    @Override
    public Group addGroup(GroupDTO groupDTO) {
        Group group = Convert.convertToEntity(groupDTO);
        group.setAccount(account);
        groupRepository.findByNameAndAccount(group.getName(), group.getAccount())
                .ifPresent(error -> {
                    throw new GroupAlreadyExistsException();
                });
        return groupRepository.save(group);
    }

    @Override
    public List<Record> findAllRecordByGroupName(String name) {
        Group existGroup = findGroupByName(name);
        List<Record> recordList = recordRepository.findByGroupAndAccount(existGroup, account);
        if (recordList.isEmpty()) {
            throw new NoRecordFoundForGroup();
        }
        return recordList;
    }

    @Override
    public Group updateGroup(GroupDTO groupDTO) {
        Group group = Convert.convertToEntity(groupDTO);
        group.setAccount(account);
        Group fetchGroup = groupRepository.findById(group.getId())
                .orElseThrow(NoGroupFoundForAccount::new);
        groupRepository.findByNameAndAccount(groupDTO.getName(), group.getAccount())
                .ifPresent(error -> {
                    throw new GroupAlreadyExistsException();
                });
        fetchGroup.setName(group.getName());
        fetchGroup.setDescription(group.getDescription());
        return groupRepository.save(fetchGroup);
    }

    @Override
    public List<Group> findAllGroups() {
        List<Group> groupList = groupRepository.findByAccount(account);
        if (groupList.isEmpty()) {
            throw new NoGroupFoundForAccount();
        }
        return groupList;
    }

    @Override
    public Group deleteGroup(String name) {
        Group existGroup = groupRepository.findByNameAndAccount(name, account)
                .orElseThrow(NoGroupFoundForAccount::new);
        List<Record> recordList = recordRepository.findByGroupAndAccount(existGroup, account);
        if (recordList.isEmpty()) {
            groupRepository.deleteById(existGroup.getId());
            return existGroup;
        } else {
            throw new GroupShouldNotContainsRecords();
        }
    }

    @Override
    public void setAccount(LoginDTO loginDTO) {
        this.account = Convert.convertToEntity(loginDTO);
    }

    @Override
    public Group findGroupByName(String name) {
        return groupRepository.findByNameAndAccount(name, account)
                .orElseThrow(NoGroupFoundForAccount::new);
    }

    @Override
    public Account getAccount() {
        return this.account;
    }
}
