package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import com.thoughtworks.rslist.service.VoteService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class VoteController {

    final
    VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable Integer rsEventId, @RequestBody VoteDto voteDto) {
        voteService.vote(rsEventId, voteDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rs/vote/list")
    public ResponseEntity<List<VoteDto>> getVoteList(@RequestParam String startTime, @RequestParam String endTime) {
//        List<VoteDto> list = voteService.getVoteList(startTime,endTime);
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        List<VoteDto> voteList = voteService.getVoteList(start, end);
        return ResponseEntity.ok(voteList);
    }
}
