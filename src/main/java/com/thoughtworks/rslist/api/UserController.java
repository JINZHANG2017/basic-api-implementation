package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.Exception.MyExcption;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto user, Errors errors){
        if(errors.hasErrors()){
            throw new MyExcption(400,"user not valid");
        }
//        users.add(user);
        userService.register(user);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id){
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/user/{id}")
    @Transactional
    public void delUserById(@PathVariable Integer id){
        userService.deleteUserById(id);
        //rsEventRespository.deleteAllByUserId(id);
    }

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getList() throws JsonProcessingException {
        List<UserDto> userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }
}
