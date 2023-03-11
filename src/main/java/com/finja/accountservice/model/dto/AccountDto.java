package com.finja.accountservice.model.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {
    @NotNull(message = "Initial Credit cannot be null")
    private BigDecimal initialCredit;

    private UUID accountId;
    private UUID customerId;
    private String title;
    private String accountType;

    private String status;

    private BigDecimal balance;

    private Boolean isLock;

    private LocalDateTime lockedAt;

    private String lockedBy;

    private LocalDateTime createdAt;

    private String createdBy;

    private List<TransactionDto> transactions;
}
