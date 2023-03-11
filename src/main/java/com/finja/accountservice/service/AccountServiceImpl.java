package com.finja.accountservice.service;

import com.finja.accountservice.client.TransactionClient;
import com.finja.accountservice.client.UserClient;
import com.finja.accountservice.common.ErrorCode;
import com.finja.accountservice.exception.AccountServiceException;
import com.finja.accountservice.mapper.AccountMapper;
import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.TransactionDto;
import com.finja.accountservice.model.dto.UserAccountInfo;
import com.finja.accountservice.model.dto.UserDto;
import com.finja.accountservice.model.entity.Account;
import com.finja.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{

    private final UserClient userClient;

    private final TransactionClient transactionClient;
    private final AccountRepository accountRepository;

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(UserClient userClient,AccountRepository accountRepository,TransactionClient transactionClient){
        this.userClient = userClient;
        this.accountRepository = accountRepository;
        this.transactionClient = transactionClient;
    }
    @Override
    @Transactional
    public AccountDto createCurrentAccount(String customerId, AccountDto accountDto) {

        try {
            ResponseEntity<UserDto> responseEntity = userClient.getUser(customerId);
            UserDto userDto = responseEntity.getBody();

            if(userDto == null){
                throw new AccountServiceException(ErrorCode.USER_NOT_FOUND);
            }

            Account account = AccountMapper.toAccount(userDto, accountDto);
            account = accountRepository.save(account);

            // If Initial Credit is greater than 0, let's create a Transaction.
            if (accountDto.getInitialCredit().compareTo(BigDecimal.ZERO) > 0) {
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setCustomerId(UUID.fromString(customerId));
                transactionDto.setAmount(accountDto.getInitialCredit());
                transactionDto.setCurrencyCode("USD");
                transactionClient.createTransaction(account.getAccountId().toString(), transactionDto);
            }

            return AccountMapper.toAccountDto(account);
        }
        catch(AccountServiceException accountServiceException){
            throw accountServiceException;
        }
        catch (Exception exception){
           throw new AccountServiceException(ErrorCode.EXTERNAL_ERROR);
        }

    }
    @Override
    public UserAccountInfo getAccountsInfo(UUID customerId, UUID accountId) {

        try {
            ResponseEntity<UserDto> responseEntity = userClient.getUser(customerId.toString());
            UserDto userDto = responseEntity.getBody();

            if (userDto == null) {
                throw new AccountServiceException(ErrorCode.USER_NOT_FOUND);
            }

            UserAccountInfo userAccountInfo = new UserAccountInfo();

            userAccountInfo.setUserName(userDto.getUserName());
            userAccountInfo.setFirstName(userDto.getFirstName());
            userAccountInfo.setLastName(userDto.getLastName());
            userAccountInfo.setMobileNo(userDto.getMobileNumber());

            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                AccountDto accountDto = AccountMapper.toAccountDto(account);

                ResponseEntity<List<TransactionDto>> transactionEntity = transactionClient.getTransactions(account.getAccountId().toString());
                List<TransactionDto> transactionDtoList = transactionEntity.getBody();
                accountDto.setTransactions(transactionDtoList);

                userAccountInfo.setAccountDto(accountDto);

            } else {
                throw new AccountServiceException(ErrorCode.ACCOUNT_NOT_FOUND);
            }
            return userAccountInfo;
        }
        catch(AccountServiceException accountServiceException){
            throw accountServiceException;
        }
        catch (Exception exception){
            throw new AccountServiceException(ErrorCode.EXTERNAL_ERROR);
        }
    }
}
