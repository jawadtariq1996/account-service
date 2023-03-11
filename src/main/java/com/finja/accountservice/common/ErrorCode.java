package com.finja.accountservice.common;

public enum ErrorCode {
    USER_NOT_FOUND("User with the given customer id does not exist"),
    ACCOUNT_NOT_FOUND("Account with the given account id does not exist"),
    EXTERNAL_ERROR("Error while connecting with External Service");

    private String message;
    ErrorCode(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
