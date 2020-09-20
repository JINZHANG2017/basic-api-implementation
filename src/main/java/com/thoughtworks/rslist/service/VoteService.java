package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Exception.MyExcption;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {
    private final UserRepository userRepository;
    private final RsEventRspository rsEventRspository;
    private final VoteRespository voteRespository;
    public VoteService(UserRepository userRepository, RsEventRspository rsEventRspository, VoteRespository voteRespository) {
        this.userRepository = userRepository;
        this.rsEventRspository = rsEventRspository;
        this.voteRespository = voteRespository;
    }



    public void vote(Integer rsEventId, VoteDto voteDto) {
        Integer userId = voteDto.getUserId();
        UserEntity userEntity = userRepository.findById(userId).get();
        int userVoteNum = userEntity.getVoteNum();
        RsEventEntity rsEventEntity = rsEventRspository.findById(rsEventId).get();
        int voteNum = voteDto.getVoteNum();
        if (userVoteNum >= voteNum) {
            VoteEntity voteEntity = VoteEntity.builder().num(voteNum)
                    .localDateTime(voteDto.getVoteTime())
                    .user(userEntity)
                    .rsEventEntity(rsEventEntity).build();
            voteRespository.save(voteEntity);
            userEntity.setVoteNum(userEntity.getVoteNum() - voteNum);
            userRepository.save(userEntity);
        } else {
            throw new MyExcption(400, "user votes not enough");
        }
    }

    public List<VoteDto> getVoteList(LocalDateTime startTime, LocalDateTime endTime) {
        List<VoteEntity> voteEntityList = voteRespository.findAllByLocalDateTimeBetween(startTime,endTime);
        List<VoteDto> voteDtoList=new ArrayList<>();
        voteEntityList.forEach(voteEntity -> voteDtoList.add(voteEntity.toVoteDto()));
        return voteDtoList;
    }
}
