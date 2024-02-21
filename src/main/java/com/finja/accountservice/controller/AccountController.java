package com.finja.accountservice.controller;

import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.UserAccountInfo;
import com.finja.accountservice.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/account/current/{customerId}")
    public ResponseEntity<AccountDto> createCurrentAccount(@PathVariable @com.finja.accountservice.common.UUID String customerId, @RequestBody @Valid AccountDto accountDto) {
        AccountDto accountDtoResponse = accountService.createCurrentAccount(customerId, accountDto);
        return ResponseEntity.ok(accountDtoResponse);
    }

    @GetMapping(value = "/customer/{customerId}/account/{accountId}")
    public ResponseEntity<UserAccountInfo> getUserAccountInformation(@PathVariable @com.finja.accountservice.common.UUID String customerId, @PathVariable @com.finja.accountservice.common.UUID String accountId) {
        UserAccountInfo userAccountInfo = accountService.getAccountsInfo(UUID.fromString(customerId), UUID.fromString(accountId));
        return ResponseEntity.ok(userAccountInfo);
    }

    private void doNothing(){
        System.out.println("This method does nothing and is just for testing.");
    }
}
