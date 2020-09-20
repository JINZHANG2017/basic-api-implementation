package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRspository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRespository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRspository rsEventRspository;
    @Autowired
    VoteRespository voteRespository;
    @Autowired
    MockMvc mockMvc;

    LocalDateTime localDateTime=LocalDateTime.now();



    @Test
    void should_vote_success_when_userVoteNum_more_than_voteNum() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900")
                .voteNum(10).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRspository.save(rsEventEntity);
        int voteNum=3;
        String json="{\"voteNum\": \""+voteNum+"\",\"userId\": \""+userEntity.getId()+"\",\"voteTime\": \""+localDateTime.toString()+"\"}";
        mockMvc.perform(post("/rs/vote/{rsEventId}",rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        UserEntity userEnt = userRepository.findById(userEntity.getId()).get();
        List<VoteEntity> votes = voteRespository.findAll();
        assertEquals(7,userEnt.getVoteNum());
        assertEquals("newuser",userEntity.getName());
        assertEquals(1,votes.size());
        assertEquals(3,votes.get(0).getNum());
    }

    @Test
    void should_return_400_when_userVoteNum_less_than_voteNum() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900")
                .voteNum(2).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRspository.save(rsEventEntity);
        int voteNum=3;
        String json="{\"voteNum\": \""+voteNum+"\",\"userId\": \""+userEntity.getId()+"\",\"voteTime\": \""+localDateTime.toString()+"\"}";
        mockMvc.perform(post("/rs/vote/{rsEventId}",rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isBadRequest()));
        UserEntity userEnt = userRepository.findById(userEntity.getId()).get();
        assertEquals(2,userEnt.getVoteNum());
        assertEquals("newuser",userEntity.getName());
    }

    @Test
    void should_get_votelist_between_starttime_and_endtime() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900")
                .voteNum(2).build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRspository.save(rsEventEntity);
        LocalDate localDate= LocalDate.now();

        VoteEntity voteEntity1=VoteEntity.builder()
                .rsEventEntity(rsEventEntity)
                .localDateTime(LocalDateTime.of(2020,9,20,17,6,30))
                .num(1)
                .user(userEntity)
                .build();
        VoteEntity voteEntity2=VoteEntity.builder()
                .rsEventEntity(rsEventEntity)
                .localDateTime(LocalDateTime.of(2020,9,20,17,6,40))
                .num(2)
                .user(userEntity)
                .build();
        VoteEntity voteEntity3=VoteEntity.builder()
                .rsEventEntity(rsEventEntity)
                .localDateTime(LocalDateTime.of(2020,9,20,17,6,50))
                .num(3)
                .user(userEntity)
                .build();
        voteRespository.save(voteEntity1);
        voteRespository.save(voteEntity2);
        voteRespository.save(voteEntity3);
        LocalDateTime startTime=LocalDateTime.of(2020,9,20,17,6,25);
        LocalDateTime endTime=LocalDateTime.of(2020,9,20,17,6,45);
//        new LocalDateTime(12);
        mockMvc.perform(get("/rs/vote/list")
                .param("startTime",startTime.toString())
                .param("endTime",endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[0].voteTime", is("2020-09-20T17:06:30")))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[1].voteTime", is("2020-09-20T17:06:40")));

    }
}