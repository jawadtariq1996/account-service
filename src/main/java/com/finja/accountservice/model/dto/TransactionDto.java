package com.finja.accountservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionDto {
    private UUID transactionId;
    private UUID customerId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private String transactionType;
    private String transactionStatus;
    private BigDecimal amount;
    private String currencyCode;
    private LocalDateTime createdAt;
    private String createdBy;
}
