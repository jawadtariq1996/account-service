package com.finja.accountservice.constant;

public enum AccountStatus {
    ACTIVE("active"),
    INACTIVE("inactive");
    private final String value;
    AccountStatus(String accountType){
        this.value = accountType;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
