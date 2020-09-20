package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.Exception.MyExcption;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.*;

@RestController
public class RsEventController {

    final
    RsEventService rsEventService;

    public RsEventController(RsEventService rsEventService) {
        this.rsEventService = rsEventService;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEventDto>> getList(@RequestParam(required = false) Integer start, @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            List<RsEventDto> list = rsEventService.getList();
            return ResponseEntity.ok(list);
        }
        List<RsEventDto> list = rsEventService.getList(start, end);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEventDto> getOneRs(@PathVariable Integer id) {
        RsEventDto rsEventDto = rsEventService.getRsById(id);
        return ResponseEntity.ok(rsEventDto);
    }

    @PostMapping("/rs/event")
    public ResponseEntity postOneRs(@Valid @RequestBody RsEventDto rsEventDto, Errors errors) throws JsonProcessingException {
        if(errors.hasErrors()){
            throw new MyExcption(400,"user not validate");
        }
        Integer id = rsEventService.addARs(rsEventDto);
        return ResponseEntity.created(null).header("index", id.toString()).build();
//        return ResponseEntity.created(null).header("index", String.valueOf(usersList.indexOf(user))).build();
    }

    @PutMapping("/rs/event")
    public ResponseEntity putOneRs(@RequestParam Integer id, @RequestBody RsEventDto rsEventDto) {
        rsEventService.putARs(id,rsEventDto);
        return ResponseEntity.status(200).build();

    }

    @DeleteMapping("/rs/event")
    public ResponseEntity delOneRs(@RequestParam Integer id) {
        rsEventService.deleteARs(id);
        return ResponseEntity.ok().build();
    }



    @PatchMapping("/rs/{id}")
    public ResponseEntity patch(@PathVariable Integer id, @RequestBody Map<String,String> params) throws JsonProcessingException {
        rsEventService.patchARs(id,params);
        return ResponseEntity.ok().build();
    }
}
