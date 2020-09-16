package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {

    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tempRsList = new ArrayList<>();
        tempRsList.add(new RsEvent("第一条事件", "无分类"));
        tempRsList.add(new RsEvent("第二条事件", "无分类"));
        tempRsList.add(new RsEvent("第三条事件", "无分类"));
        return tempRsList;
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{id}")
    public RsEvent getOneRs(@PathVariable Integer id) {
        return rsList.get(id - 1);
    }

    @PostMapping("/rs/event")
    public void postOneRs(@RequestBody String rsEventStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(rsEventStr, RsEvent.class);
        rsList.add(rsEvent);
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
}
