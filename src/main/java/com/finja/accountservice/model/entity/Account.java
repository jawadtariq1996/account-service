package com.finja.accountservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "customerId", nullable = false)
    private UUID customerId;
    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "account_type",nullable = false)
    private String accountType;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "balance",nullable = false)
    private BigDecimal balance;

    @Column(name = "is_lock")
    private Boolean isLock;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "locked_by")
    private String lockedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

}

