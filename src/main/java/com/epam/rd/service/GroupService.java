package com.epam.rd.service;

import com.epam.rd.dto.GroupDTO;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.entity.Group;
import com.epam.rd.entity.Record;
import com.epam.rd.exception.*;

import java.util.List;

public interface GroupService {
    Group addGroup(GroupDTO groupDTO) throws GroupAlreadyExistsException;

    List<Record> findAllRecordByGroupName(String name) throws NoRecordFoundForGroup, NoGroupFoundForAccount;

    Group updateGroup(GroupDTO groupDTO) throws NoGroupFoundForAccount;

    List<Group> findAllGroups() throws NoGroupFoundForAccount;

    Group deleteGroup(String name) throws NoGroupFoundForAccount, GroupShouldNotContainsRecords;

    void setAccount(LoginDTO LoginDTO);

    Group findGroupByName(String name) throws NoGroupFoundForAccount;

    Account getAccount();
}
