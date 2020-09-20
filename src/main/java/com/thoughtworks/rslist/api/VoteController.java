package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoteController {
    private final UserRepository userRepository;
    private final RsEventRspository rsEventRspository;
    private final VoteRespository voteRespository;

    public VoteController(UserRepository userRepository, RsEventRspository rsEventRspository, VoteRespository voteRespository) {
        this.userRepository = userRepository;
        this.rsEventRspository = rsEventRspository;
        this.voteRespository = voteRespository;
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity vote(@PathVariable Integer rsEventId, @RequestBody VoteDto voteDto) {
        Integer userId = voteDto.getUserId();
        UserEntity userEntity=userRepository.findById(userId).get();
        int userVoteNum = userEntity.getVoteNum();
        RsEventEntity rsEventEntity = rsEventRspository.findById(rsEventId).get();
        int voteNum=voteDto.getVoteNum();
        if(userVoteNum>=voteNum){
            VoteEntity voteEntity = VoteEntity.builder().num(voteNum)
                    .localDateTime(voteDto.getVoteTime())
                    .user(userEntity)
                    .rsEventEntity(rsEventEntity).build();
            voteRespository.save(voteEntity);
            userEntity.setVoteNum(userEntity.getVoteNum()-voteNum);
            userRepository.save(userEntity);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
