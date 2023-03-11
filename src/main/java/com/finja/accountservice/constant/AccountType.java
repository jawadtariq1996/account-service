package com.finja.accountservice.constant;

public enum AccountType {

    SAVING("saving"),
    CURRENT("current"),
    LOAN("loan");
    private final String value;
     AccountType(String accountType){
        this.value = accountType;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
