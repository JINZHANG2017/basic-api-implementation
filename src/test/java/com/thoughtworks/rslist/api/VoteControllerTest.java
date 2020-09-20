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

import java.time.LocalDateTime;
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
    //    添加投票接口
//    ```
//    request: post /rs/vote/{rsEventId}
//    request body: {
//        voteNum: 5,
//                userId: 1,
//                voteTime: "current time"
//    }
//    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
//    如果用户剩的票数小于voteNum,则投票失败，返回400
//    考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
//    创建一个Vote表是一个明智的选择
//            目前不用考虑给热搜事件列表排序的问题
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
}