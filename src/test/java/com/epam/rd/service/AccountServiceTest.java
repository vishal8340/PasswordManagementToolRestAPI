package com.epam.rd.service;

import com.epam.rd.dto.LoginDTO;
import com.epam.rd.dto.RegisterDTO;
import com.epam.rd.entity.Account;
import com.epam.rd.exception.AccountAlreadyExistsException;
import com.epam.rd.exception.AccountDoesNotExistException;
import com.epam.rd.exception.WrongPasswordException;
import com.epam.rd.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    AccountServiceImpl accountDaoImpl;

    @Mock
    AccountRepository accountRepository;

    @Test
    void testRegisterAccountThrowExceptionWhenAccountAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("Vishal Kumar", "KGR009517", "Vishal834019");
        when(accountRepository.findByUserName(any())).thenReturn(Optional.of(new Account()));
        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> accountDaoImpl.registerAccount(registerDTO));
    }

    @Test
    void testRegisterAccountDoesNotThrowExceptionWhileNewAccountRegistration() {
        RegisterDTO registerDTO = new RegisterDTO("Vishal Kumar", "KGR009517", "Vishal834019");
        Account account = new Account();
        when(accountRepository.save(any())).thenReturn(account);
        when(accountRepository.findByUserName(any())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> accountDaoImpl.registerAccount(registerDTO));
    }


    @Test
    void validateLoginThrowExceptionWhenAccountDoesNotExist() {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        when(accountRepository.findByUserName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountDoesNotExistException.class, () -> accountDaoImpl.validateLogin(loginDTO));
    }

    @Test
    void validateLoginThrowExceptionWhenWrongPasswordException() {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        Optional<Account> account = Optional.of(new Account("KGR009517", "Vishal834019"));
        when(accountRepository.findByUserName(any())).thenReturn(account);
        Assertions.assertThrows(WrongPasswordException.class, () -> accountDaoImpl.validateLogin(loginDTO));
    }

    @Test
    void validateLoginDoesNotThrowExceptionWhileAccountExist() {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        Optional<Account> account = Optional.of(new Account("KGR009517", "Vishal834019"));
        account.get().setPassword(sha256Hex(account.get().getPassword()));
        when(accountRepository.findByUserName(any())).thenReturn(account);
        Assertions.assertDoesNotThrow(() -> accountDaoImpl.validateLogin(loginDTO));
    }

    @Test
    void validateLoginReturnAccountNameWhileAccountExist() throws AccountDoesNotExistException, WrongPasswordException {
        LoginDTO loginDTO = new LoginDTO("KGR009517", "Vishal834019");
        Optional<Account> account = Optional.of(new Account("Vishal Kumar", "KGR009517", "Vishal834019"));
        account.get().setPassword(sha256Hex(account.get().getPassword()));
        when(accountRepository.findByUserName(any())).thenReturn(account);
        Assertions.assertEquals("Vishal Kumar", accountDaoImpl.validateLogin(loginDTO));
    }
}