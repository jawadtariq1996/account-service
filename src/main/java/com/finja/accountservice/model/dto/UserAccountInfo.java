package com.finja.accountservice.model.dto;

import lombok.Data;


@Data
public class UserAccountInfo {

    private String firstName;
    private String lastName;
    private String userName;
    private String mobileNo;
    private AccountDto accountDto;

}
