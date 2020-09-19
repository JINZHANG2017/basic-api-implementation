package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.util.JsonHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.*;

@RestController
public class RsController {

    private List<UserDto> usersList = initUsersList();
    private List<RsEventDto> rsList = initRsList();

    private final UserRepository userRepository;

    private final RsEventRespository rsEventRespository;

    public RsController(RsEventRespository rsEventRespository, UserRepository userRepository) {
        this.rsEventRespository = rsEventRespository;
        this.userRepository = userRepository;
    }

    private List<RsEventDto> initRsList() {
        List<RsEventDto> tempRsList = new ArrayList<>();

//        tempRsList.add(new RsEvent("第一条事件", "无分类", usersList.get(0)));
//        tempRsList.add(new RsEvent("第二条事件", "无分类", usersList.get(1)));
//        tempRsList.add(new RsEvent("第三条事件", "无分类", usersList.get(2)));
        return tempRsList;
    }

    private List<UserDto> initUsersList() {
        List<UserDto> tempUserList = new ArrayList<>();
        UserDto user1 = new UserDto("jack", "男", 20, "123@test.com", "1334567890");
        UserDto user2 = new UserDto("tom", "男", 20, "123@test.com", "1334567890");
        UserDto user3 = new UserDto("trump", "男", 20, "123@test.com", "1334567890");
        tempUserList.add(user1);
        tempUserList.add(user2);
        tempUserList.add(user3);
        return tempUserList;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEventDto>> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            List<RsEventEntity> allRsEventEntities = rsEventRespository.findAll();
            List<RsEventDto> allRsEventDtos=new ArrayList<>();
            allRsEventEntities.forEach(item->allRsEventDtos.add(item.toRsEventDto()));
            return ResponseEntity.ok(allRsEventDtos);
        }
        List<RsEventEntity> allRsEventEntities = rsEventRespository.findAllByIdBetween(start, end);
        List<RsEventDto> allRsEventDtos=new ArrayList<>();
        allRsEventEntities.forEach(item->allRsEventDtos.add(item.toRsEventDto()));
        return ResponseEntity.ok(allRsEventDtos);
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEventDto> getOneRs(@PathVariable Integer id) {
//        return ResponseEntity.ok(rsList.get(id - 1));
        Optional<RsEventEntity> result = rsEventRespository.findById(id);
        if (!result.isPresent()) {
//            throw new RequestNotValidException("invalid id");
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = result.get();
        UserEntity userEntity =rsEventEntity.getUser();
        return ResponseEntity.ok(RsEventDto.builder()
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyWord())
                .user(new UserDto(userEntity.getName(),
                        userEntity.getGender(),
                        userEntity.getAge(),
                        userEntity.getEmail(),
                        userEntity.getPhone()))
                .build());
    }

    @PostMapping("/rs/event")
    public ResponseEntity postOneRs(@Valid @RequestBody RsEventDto rsEventDto) throws JsonProcessingException {
        if(rsEventDto.getUserId()==null){
            return ResponseEntity.badRequest().build();
        }
        if (!userRepository.existsById(rsEventDto.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        UserEntity userEntity = userRepository.findById(rsEventDto.getUserId()).get();
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEventDto.getEventName())
                .keyWord(rsEventDto.getKeyWord())
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
//        rsList.add(rsEvent);

        return ResponseEntity.created(null).header("index", rsEventEntity.getId().toString()).build();
//        return ResponseEntity.created(null).header("index", String.valueOf(usersList.indexOf(user))).build();
    }

    @PutMapping("/rs/event")
    public ResponseEntity putOneRs(@RequestParam Integer id, @RequestBody RsEventDto rsEventDto) {
//        RsEventDto originRsEventDto = rsList.get(id - 1);
        RsEventEntity rsEventEntity=rsEventRespository.findById(id).get();
        if (rsEventDto.getEventName() != null) {
            //originRsEventDto.setEventName(rsEventDto.getEventName());
            rsEventEntity.setEventName(rsEventDto.getEventName());
            rsEventRespository.save(rsEventEntity);
        }
        if (rsEventDto.getKeyWord() != null) {
//            originRsEventDto.setKeyWord(rsEventDto.getKeyWord());
            rsEventEntity.setKeyWord(rsEventDto.getKeyWord());
            rsEventRespository.save(rsEventEntity);
        }
        return ResponseEntity.status(200).build();

    }

    @DeleteMapping("/rs/event")
    public ResponseEntity delOneRs(@RequestParam Integer id) {
        rsEventRespository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/list")
    public ResponseEntity<String> getList() throws JsonProcessingException {
        return ResponseEntity.ok(JsonHelper.getString(usersList));
    }

    @PatchMapping("/rs/{id}")
    public ResponseEntity patch(@PathVariable Integer id, @RequestBody Map<String,String> params) throws JsonProcessingException {
//        ,@RequestBody String eventName,@RequestBody String keyWord,@RequestBody Integer userId
        Optional<RsEventEntity> rsEventEntityOpt = rsEventRespository.findById(id);
        if(!rsEventEntityOpt.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = rsEventEntityOpt.get();
        if(!rsEventEntity.getUser().getId().toString().equals((params.get("userId")))){
            return ResponseEntity.badRequest().build();
        }
        if(params.keySet().contains("keyWord")&&!"".equals(params.get("keyWord"))){
            rsEventEntity.setKeyWord(params.get("keyWord"));
        }
        if(params.keySet().contains("eventName")&&!"".equals(params.get("eventName"))){
            rsEventEntity.setEventName(params.get("eventName"));
        }
        rsEventRespository.save(rsEventEntity);
        return ResponseEntity.ok().build();
    }
}
