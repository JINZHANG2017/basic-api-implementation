package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    final
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto user){

//        users.add(user);
        UserEntity userEntity=UserEntity.builder()
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Integer id){
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(!userEntity.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userEntity.get());
    }

}
