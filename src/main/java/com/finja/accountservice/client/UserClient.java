package com.finja.accountservice.client;

import com.finja.accountservice.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "user-service", url = "http://localhost:8083/")
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}", produces = "application/json")
    ResponseEntity<UserDto> getUser(@PathVariable String userId);
}
