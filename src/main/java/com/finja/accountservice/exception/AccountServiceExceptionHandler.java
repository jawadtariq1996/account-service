package com.finja.accountservice.exception;

import com.finja.accountservice.common.ErrorCode;
import com.finja.accountservice.common.ErrorResponse;
import com.finja.accountservice.common.ValidationErrorDto;
import com.finja.accountservice.common.ValidationErrors;
import feign.RetryableException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    public static final ErrorResponse USER_DOES_NOT_FOUND = new ErrorResponse("User Does Not Exist in the Database");
    public static final ErrorResponse COULD_NOT_CONNECT = new ErrorResponse("Could not connect with the External Server");


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> noSuchElementExceptionHandler(){

      return new ResponseEntity<>(USER_DOES_NOT_FOUND,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccountServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> accountServiceException(AccountServiceException accountServiceException){

        ErrorCode errorCode = accountServiceException.getErrorCode();
        switch (errorCode){

            case EXTERNAL_ERROR -> {return new ResponseEntity<>(new ErrorResponse(errorCode.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);}
            default -> { return new ResponseEntity<>(new ErrorResponse(errorCode.getMessage()),HttpStatus.BAD_REQUEST);}
        }
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> emptyResultDataAccessException(){
        return new ResponseEntity<>("User Does not Exist in the Database. Please Provide a Valid Id",HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDto handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException){

        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        validationErrorDto.setValidationErrorsList(methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrors(fieldError.getField(),fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return validationErrorDto;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDto handleConstraintViolationException(ConstraintViolationException constraintViolationException){
        return new ValidationErrorDto(constraintViolationException);
    }
}
