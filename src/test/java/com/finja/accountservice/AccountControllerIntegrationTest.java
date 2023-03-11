package com.finja.accountservice;

import com.finja.accountservice.client.TransactionClient;
import com.finja.accountservice.client.UserClient;
import com.finja.accountservice.constant.AccountType;
import com.finja.accountservice.mapper.AccountMapper;
import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.TransactionDto;
import com.finja.accountservice.model.dto.UserAccountInfo;
import com.finja.accountservice.model.dto.UserDto;
import com.finja.accountservice.model.entity.Account;
import com.finja.accountservice.repository.AccountRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserClient userClient;

    @MockBean
    private TransactionClient transactionClient;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void createCurrentAccount_ReturnsCreatedAccount() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountType(AccountType.CURRENT.toString());
        accountDto.setInitialCredit(new BigDecimal("1000.00"));

        Account account = AccountMapper.toAccount(new UserDto(), accountDto);
        account.setAccountId(accountId);

        UserDto userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setMobileNumber("+1234567890");

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(UUID.randomUUID());
        transactionDto.setCustomerId(customerId);
        transactionDto.setToAccountId(accountId);
        transactionDto.setAmount(new BigDecimal("100.00"));
        transactionDto.setCurrencyCode("USD");


        given(userClient.getUser(customerId.toString())).willReturn(new ResponseEntity<>(userDto, HttpStatus.OK));
        given(accountRepository.save(any(Account.class))).willReturn(account);
        doReturn( new ResponseEntity<>(transactionDto, HttpStatus.OK)).when(transactionClient).createTransaction(accountId.toString(),transactionDto);


        // Act
        ResponseEntity<AccountDto> responseEntity = restTemplate.postForEntity(
                "/account/current/{customerId}",
                accountDto,
                AccountDto.class,
                customerId
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(AccountMapper.toAccountDto(account));
    }

    @Test
    public void getUserAccountInformation_ReturnsUserAccountInformation() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        UserDto userDto = new UserDto();
        userDto.setUserName("testuser");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setMobileNumber("+1234567890");

        Account account = AccountMapper.toAccount(userDto, new AccountDto());
        account.setAccountId(accountId);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(UUID.randomUUID());
        transactionDto.setCustomerId(customerId);
        transactionDto.setToAccountId(accountId);
        transactionDto.setAmount(new BigDecimal("100.00"));
        transactionDto.setCurrencyCode("USD");

        given(userClient.getUser(customerId.toString())).willReturn(new ResponseEntity<>(userDto, HttpStatus.OK));
        given(accountRepository.findById(accountId)).willReturn(Optional.of(account));
        doReturn( new ResponseEntity<>(Collections.singletonList(transactionDto), HttpStatus.OK)).when(transactionClient).getTransactions(accountId.toString());

        // Act
        ResponseEntity<UserAccountInfo> responseEntity = restTemplate.getForEntity(
                "/customer/{customerId}/account/{accountId}",
                UserAccountInfo.class,
                customerId,
                accountId
        );

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserAccountInfo userAccountInfo = responseEntity.getBody();
        assertThat(userAccountInfo.getUserName()).isEqualTo(userDto.getUserName());
        assertThat(userAccountInfo.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(userAccountInfo.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(userAccountInfo.getMobileNo()).isEqualTo(userDto.getMobileNumber());
        assertThat(userAccountInfo.getAccountDto().getTransactions()).hasSize(1);
    }
}

