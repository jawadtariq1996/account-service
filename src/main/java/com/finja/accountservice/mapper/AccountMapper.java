package com.finja.accountservice.mapper;

import com.finja.accountservice.constant.AccountConstants;
import com.finja.accountservice.constant.AccountStatus;
import com.finja.accountservice.constant.AccountType;
import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.UserDto;
import com.finja.accountservice.model.entity.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountMapper {

    public static Account toAccount(UserDto userDto, AccountDto accountDto){

        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setCustomerId(userDto.getUserId());
        account.setTitle(userDto.getFirstName() + userDto.getLastName());
        account.setStatus(AccountStatus.ACTIVE.toString());
        account.setAccountType(AccountType.CURRENT.toString());
        account.setIsLock(false);
        account.setBalance(accountDto.getInitialCredit());
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy(AccountConstants.CREATED_BY_USER);

        return account;
    }

    public static AccountDto toAccountDto(Account account){

        AccountDto accountDto = new AccountDto();

        accountDto.setAccountId(account.getAccountId());
        accountDto.setCustomerId(account.getCustomerId());
        accountDto.setTitle(account.getTitle());
        accountDto.setInitialCredit(account.getBalance());
        accountDto.setStatus(account.getStatus());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setIsLock(account.getIsLock());
        accountDto.setBalance(account.getBalance());
        accountDto.setCreatedAt(account.getCreatedAt());
        accountDto.setCreatedBy(account.getCreatedBy());

        return accountDto;
    }
}
