package com.finja.accountservice.service;

import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.UserAccountInfo;

import java.util.UUID;

public interface AccountService {

    AccountDto createCurrentAccount(String customerId, AccountDto accountDto);

    UserAccountInfo getAccountsInfo(UUID customerId, UUID accountId);
}
