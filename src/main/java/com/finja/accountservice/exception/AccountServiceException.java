package com.finja.accountservice.exception;

import com.finja.accountservice.common.ErrorCode;
import lombok.Data;

@Data
public class AccountServiceException extends RuntimeException{
    private ErrorCode errorCode;
    public  AccountServiceException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


}
