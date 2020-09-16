package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.util.JsonHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    private List<UserDto> usersList = initUsersList();
    private List<RsEvent> rsList = initRsList();


    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();

        tempRsList.add(new RsEvent("第一条事件", "无分类", usersList.get(0)));
        tempRsList.add(new RsEvent("第二条事件", "无分类", usersList.get(1)));
        tempRsList.add(new RsEvent("第三条事件", "无分类", usersList.get(2)));
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
    public ResponseEntity<List<RsEvent>> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            return ResponseEntity.ok(rsList);
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEvent> getOneRs(@PathVariable Integer id) {
        return ResponseEntity.ok(rsList.get(id - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity postOneRs(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        UserDto user = rsEvent.getUser();
        if (user != null) {
            if (usersList.stream().filter(u -> u.getName().equals(user.getName())).count() == 0) {
                usersList.add(user);
            }
        }
        rsList.add(rsEvent);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/rs/event")
    public void putOneRs(@RequestParam Integer id, @RequestBody RsEvent rsEvent) {
        RsEvent originRsEvent = rsList.get(id - 1);
        if (rsEvent.getEventName() != null) {
            originRsEvent.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            originRsEvent.setKeyWord(rsEvent.getKeyWord());
        }
    }

    @DeleteMapping("/rs/event")
    public void delOneRs(@RequestParam Integer id) {
        RsEvent rsEvent = rsList.get(id - 1);
        rsList.remove(rsEvent);
    }

    @GetMapping("/user/list")
    public String getList() throws JsonProcessingException {
        return JsonHelper.getString(usersList);
    }
}
