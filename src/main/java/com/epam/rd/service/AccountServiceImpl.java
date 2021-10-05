package com.epam.rd.service;

import com.epam.rd.converter.Convert;
import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RegisterDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.exception.AccountAlreadyExistsException;
import com.epam.rd.exception.AccountDoesNotExistException;
import com.epam.rd.exception.WrongPasswordException;
import com.epam.rd.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account registerAccount(RegisterDTO registerDTO) {
        Account accountConverted = Convert.convertToEntity(registerDTO);
        accountRepository.findByUserName(accountConverted.getUserName())
                .ifPresent(error -> {
                    throw new AccountAlreadyExistsException();
                });
        accountConverted.setPassword(sha256Hex(accountConverted.getPassword()));
        return accountRepository.save(accountConverted);
    }

    @Override
    public String validateLogin(LoginDTO loginDTO) {
        Account account = Convert.convertToEntity(loginDTO);
        account.setPassword(sha256Hex(account.getPassword()));
        Account accountByUserName = accountRepository.findByUserName(account.getUserName())
                .orElseThrow(AccountDoesNotExistException::new);
        if (accountByUserName.getPassword().equals(account.getPassword())) {
            return accountByUserName.getAccountName();
        } else {
            throw new WrongPasswordException();
        }
    }
}
