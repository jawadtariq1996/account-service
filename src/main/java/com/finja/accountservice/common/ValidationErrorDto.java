package com.finja.accountservice.common;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ValidationErrorDto {

    private List<ValidationErrors> validationErrorsList;

    public ValidationErrorDto(ConstraintViolationException constraintViolationException){
        validationErrorsList = constraintViolationException.getConstraintViolations().stream().
                                                          map(x-> new ValidationErrors(x.getPropertyPath().toString(),x.getMessage()))
                                                          .collect(Collectors.toList());
    }

}
