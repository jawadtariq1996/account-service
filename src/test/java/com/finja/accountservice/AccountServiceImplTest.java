package com.finja.accountservice;

import com.finja.accountservice.client.TransactionClient;
import com.finja.accountservice.client.UserClient;
import com.finja.accountservice.constant.AccountType;
import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.TransactionDto;
import com.finja.accountservice.model.dto.UserAccountInfo;
import com.finja.accountservice.model.dto.UserDto;
import com.finja.accountservice.model.entity.Account;
import com.finja.accountservice.repository.AccountRepository;
import com.finja.accountservice.service.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class AccountServiceImplTest {

    @Mock
    private UserClient userClient;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void testCreateCurrentAccount() {

        // Given
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        BigDecimal initialCredit = new BigDecimal("100.00");
        AccountDto accountDto = new AccountDto();
        accountDto.setInitialCredit(initialCredit);

        UserDto userDto = new UserDto();
        userDto.setUserId(customerId);
        userDto.setUserName("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setMobileNumber("1234567890");

        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setCustomerId(customerId);
        account.setAccountType(AccountType.CURRENT.toString());
        account.setBalance(initialCredit);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCustomerId(customerId);
        transactionDto.setAmount(initialCredit);
        transactionDto.setCurrencyCode("USD");

        given(userClient.getUser(customerId.toString())).willReturn(ResponseEntity.ok(userDto));
        given(accountRepository.save(any(Account.class))).willReturn(account);
        given(transactionClient.createTransaction(anyString(), any(TransactionDto.class))).willReturn(ResponseEntity.ok(transactionDto));

        // When
        AccountDto createdAccountDto = accountService.createCurrentAccount(customerId.toString(), accountDto);

        // Then
        assertNotNull(createdAccountDto);
        assertEquals(initialCredit, createdAccountDto.getBalance());
        assertEquals(account.getAccountId(), createdAccountDto.getAccountId());
        assertEquals(account.getCustomerId(), createdAccountDto.getCustomerId());
        assertEquals(account.getAccountType(), createdAccountDto.getAccountType());



    }

    @Test
    public void testGetAccountsInfo() {

        // Given
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        UserDto userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setMobileNumber("1234567890");

        Account account = new Account();
        account.setAccountId(accountId);
        account.setCustomerId(customerId);
        account.setAccountType(AccountType.CURRENT.toString());
        account.setBalance(new BigDecimal("100.00"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(UUID.randomUUID());
        transactionDto.setCustomerId(customerId);
        transactionDto.setAmount(new BigDecimal("50.00"));
        transactionDto.setCurrencyCode("USD");

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(transactionDto);

        given(userClient.getUser(customerId.toString())).willReturn(ResponseEntity.ok(userDto));
        given(accountRepository.findById(accountId)).willReturn(Optional.of(account));
        given(transactionClient.getTransactions(accountId.toString())).willReturn(ResponseEntity.ok(transactionDtoList));

        // When
        UserAccountInfo userAccountInfo = accountService.getAccountsInfo(customerId, accountId);

        // Then
        assertNotNull(userAccountInfo);
        assertEquals(userDto.getUserName(), userAccountInfo.getUserName());
        assertEquals(userDto.getFirstName(), userAccountInfo.getFirstName());
        assertEquals(userDto.getLastName(), userAccountInfo.getLastName());
        assertEquals(userDto.getMobileNumber(), userAccountInfo.getMobileNo());
        assertNotNull(userAccountInfo.getAccountDto());
        assertEquals(account.getAccountId(), userAccountInfo.getAccountDto().getAccountId());
        assertEquals(account.getCustomerId(), userAccountInfo.getAccountDto().getCustomerId());
        assertEquals(account.getAccountType(), userAccountInfo.getAccountDto().getAccountType());
        assertEquals(account.getBalance(), userAccountInfo.getAccountDto().getBalance());

    }
}