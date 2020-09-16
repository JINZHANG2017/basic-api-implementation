package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.util.JsonHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<UserDto> users=new ArrayList<>();

//    private static List<UserDto> initUsersList(){
//        List<UserDto> tempUsers=new ArrayList<>();
//        UserDto user1=new UserDto("jack","男",20,"123@test.com","1334567890");
//        UserDto user2=new UserDto("tom","男",20,"123@test.com","1334567890");
//        UserDto user3=new UserDto("trump","男",20,"123@test.com","1334567890");
//        tempUsers.add(user1);
//        tempUsers.add(user2);
//        tempUsers.add(user3);
//        return tempUsers;
//    }
    @PostMapping("/user/register")
    public void register(@Valid @RequestBody UserDto user){
        users.add(user);
    }


}
