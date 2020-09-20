package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Exception.MyExcption;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RsEventService {
    final
    RsEventRspository rsEventRepository;
    final
    UserRepository userRepository;
    final
    VoteRespository voteRepository;

    public RsEventService(RsEventRspository rsEventRepository, UserRepository userRepository, VoteRespository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public List<RsEventDto> getList(){
        List<RsEventEntity> allRsEventEntities = rsEventRepository.findAll();
        List<RsEventDto> allRsEventDtos=new ArrayList<>();
        allRsEventEntities.forEach(item->allRsEventDtos.add(item.toRsEventDto()));
        return allRsEventDtos;
    }

    public List<RsEventDto> getList(int start,int end){
        List<RsEventEntity> allRsEventEntities = rsEventRepository.findAllByIdBetween(start, end);
        List<RsEventDto> allRsEventDtos=new ArrayList<>();
        allRsEventEntities.forEach(item->allRsEventDtos.add(item.toRsEventDto()));
        return allRsEventDtos;
    }

    public RsEventDto getRsById(int id){
        Optional<RsEventEntity> result = rsEventRepository.findById(id);
        if (!result.isPresent()) {
            throw new RuntimeException();
        }
        RsEventEntity rsEventEntity = result.get();
        UserEntity userEntity =rsEventEntity.getUser();
        return RsEventDto.builder()
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .user(new UserDto(userEntity.getName(),
                        userEntity.getGender(),
                        userEntity.getAge(),
                        userEntity.getEmail(),
                        userEntity.getPhone()))
                .build();
    }

    public int addARs(RsEventDto rsEventDto){
        if(rsEventDto.getUserId()==null){
            throw new MyExcption(400,"userId is null");
        }
        if (!userRepository.existsById(rsEventDto.getUserId())) {
            throw new MyExcption(400,"userId doesnot exist in db");
        }
        UserEntity userEntity = userRepository.findById(rsEventDto.getUserId()).get();
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEventDto.getEventName())
                .keyWord(rsEventDto.getKeyWord())
                .user(userEntity)
                .build();
        rsEventRepository.save(rsEventEntity);
        return rsEventEntity.getId();
    }

    public void putARs(int id,RsEventDto rsEventDto){
        RsEventEntity rsEventEntity= rsEventRepository.findById(id).get();
        if (rsEventDto.getEventName() != null) {
            //originRsEventDto.setEventName(rsEventDto.getEventName());
            rsEventEntity.setEventName(rsEventDto.getEventName());
        }
        if (rsEventDto.getKeyWord() != null) {
//            originRsEventDto.setKeyWord(rsEventDto.getKeyWord());
            rsEventEntity.setKeyWord(rsEventDto.getKeyWord());
        }
        rsEventRepository.save(rsEventEntity);
    }

    public void deleteARs(int id){
        rsEventRepository.deleteById(id);
    }

    public void patchARs( Integer id, Map<String,String> params){
        Optional<RsEventEntity> rsEventEntityOpt = rsEventRepository.findById(id);
        if(!rsEventEntityOpt.isPresent()){
            throw new RuntimeException();
        }
        RsEventEntity rsEventEntity = rsEventEntityOpt.get();
        if(!rsEventEntity.getUser().getId().toString().equals((params.get("userId")))){
            throw new MyExcption(400,"userID not matches");
        }
        if(params.keySet().contains("keyWord")&&!"".equals(params.get("keyWord"))){
            rsEventEntity.setKeyWord(params.get("keyWord"));
        }
        if(params.keySet().contains("eventName")&&!"".equals(params.get("eventName"))){
            rsEventEntity.setEventName(params.get("eventName"));
        }
        rsEventRepository.save(rsEventEntity);
    }
}
