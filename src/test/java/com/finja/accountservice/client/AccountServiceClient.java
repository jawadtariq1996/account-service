package com.finja.accountservice.client;

import com.finja.accountservice.model.dto.AccountDto;
import com.finja.accountservice.model.dto.UserAccountInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "account-service")
public interface AccountServiceClient {

    @PostMapping(value = "/account/current/{customerId}")
    ResponseEntity<AccountDto> createCurrentAccount(@PathVariable String customerId, @RequestBody AccountDto accountDto);

    @GetMapping(value = "/customer/{customerId}/account/{accountId}")
    ResponseEntity<UserAccountInfo> getUserAccountInformation(@PathVariable String customerId, @PathVariable String accountId);

}