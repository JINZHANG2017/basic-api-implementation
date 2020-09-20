package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Exception.MyExcption;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final
    UserRepository userRepository;

    public UserService(UserRepository userRepository, RsEventRspository rsEventRspository) {
        this.userRepository = userRepository;
        this.rsEventRspository = rsEventRspository;
    }

    final
    RsEventRspository rsEventRspository;

    public void register(UserDto user){
        UserEntity userEntity=UserEntity.builder()
                .age(user.getAge())
                .email(user.getEmail())
                .gender(user.getGender())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
        userRepository.save(userEntity);
    }

    public UserDto getUserById(int id){
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(!userEntity.isPresent()){
            throw  new MyExcption(400,"user not found");
        }
        return userEntity.get().toUserDto();
    }

    public void deleteUserById(int id){
        userRepository.deleteById(id);
    }

    public List<UserDto> getUserList(){
        List<UserEntity> allUserEntities = userRepository.findAll();
        List<UserDto> allUserDtos=new ArrayList<>();
        allUserEntities.forEach(item->allUserDtos.add(item.toUserDto()));
        return allUserDtos;
    }
}
