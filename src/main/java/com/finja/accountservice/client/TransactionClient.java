package com.finja.accountservice.client;

import com.finja.accountservice.model.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "transaction-service", url = "http://localhost:8084/")
public interface TransactionClient {
    @RequestMapping(method = RequestMethod.POST, value = "/transaction/{accountId}", produces = "application/json")
    ResponseEntity<TransactionDto> createTransaction(@PathVariable String accountId, @RequestBody TransactionDto transactionDto);

    @RequestMapping(method = RequestMethod.GET, value = "/transactions", produces = "application/json")
    ResponseEntity<List<TransactionDto>> getTransactions(@RequestParam String accountId);
}
