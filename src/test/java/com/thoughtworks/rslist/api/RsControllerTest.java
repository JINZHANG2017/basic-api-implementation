package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.util.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    void add3RsToDB() {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity1 = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key0")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity1);
        RsEventEntity rsEventEntity2 = RsEventEntity.builder()
                .eventName("event 1")
                .keyWord("key1")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity2);
        RsEventEntity rsEventEntity3 = RsEventEntity.builder()
                .eventName("event 2")
                .keyWord("key2")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity3);
    }

    @Test
    void should_get_rslist() throws Exception {
        add3RsToDB();
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].keyWord", is("key0")))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].keyWord", is("key1")))
                .andExpect(jsonPath("$[2].eventName", is("event 2")))
                .andExpect(jsonPath("$[2].keyWord", is("key2")));
    }

    @Test
    void should_get_one_rs() throws Exception {
        add3RsToDB();
        mockMvc.perform(get("/rs/2"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("key0")));
        mockMvc.perform(get("/rs/3"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 1")))
                .andExpect(jsonPath("$.keyWord", is("key1")));
        mockMvc.perform(get("/rs/4"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 2")))
                .andExpect(jsonPath("$.keyWord", is("key2")));
    }

    @Test
    void should_get_range_rs() throws Exception {
        add3RsToDB();
        mockMvc.perform(get("/rs/list?start=2&end=4"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].keyWord", is("key0")))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].keyWord", is("key1")))
                .andExpect(jsonPath("$[2].eventName", is("event 2")))
                .andExpect(jsonPath("$[2].keyWord", is("key2")));
    }

    @Test
    void should_add_one_rs_event() throws Exception {

//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        UserEntity userEntity = UserEntity.builder()

                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        UserDto userDto = userEntity.toUserDto();
        RsEventDto rsEventDto = RsEventDto.builder()
                .eventName("event 0")
                .keyWord("key0")
                .user(userDto)
                .userId(userEntity.getId())
                .build();
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventEntityList = rsEventRespository.findAll();
        assertEquals(1, rsEventEntityList.size());
        assertEquals("event 0", rsEventEntityList.get(0).getEventName());
        assertEquals("key0", rsEventEntityList.get(0).getKeyWord());

    }

    @Test
    void should_put_a_rs_eventName_is_null() throws Exception {
        add3RsToDB();
        RsEventDto rsEventDto = new RsEventDto(null, "政治");
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform((put("/rs/event?id=2")).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        RsEventEntity rsEventEntity = rsEventRespository.findById(2).get();
        assertEquals("政治", rsEventEntity.getKeyWord());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("event 0")))
//                .andExpect(jsonPath("$[0].keyWord", is("政治")));
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

    }

    @Test
    void should_put_a_rs_keyWord_is_null() throws Exception {
        add3RsToDB();
        RsEventDto rsEventDto = new RsEventDto("新事件", null);
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform((put("/rs/event?id=2")).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        RsEventEntity rsEventEntity = rsEventRespository.findById(2).get();
        assertEquals("新事件", rsEventEntity.getEventName());

    }

    @Test
    void should_put_a_rs3() throws Exception {
        add3RsToDB();
        RsEventDto rsEventDto = new RsEventDto("新事件", "政治");
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform((put("/rs/event?id=2")).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        RsEventEntity rsEventEntity = rsEventRespository.findById(2).get();
        assertEquals("新事件", rsEventEntity.getEventName());
        assertEquals("政治", rsEventEntity.getKeyWord());
    }

    @Test
    void should_delete_one_rs() throws Exception {
        add3RsToDB();
        mockMvc.perform((delete("/rs/event?id=2")))
                .andExpect((status().isOk()));
        List<RsEventEntity> rsEventEntityList = rsEventRespository.findAll();
        assertEquals(2,rsEventEntityList.size());
        assertEquals("event 1",rsEventEntityList.get(0).getEventName());
        assertEquals("event 2",rsEventEntityList.get(1).getEventName());
    }

//    @Test
//    void should_add_one_rs_event_when_user_exists() throws Exception {
//        mockMvc.perform(get("/user/list"))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(get("/rs/list"))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$", hasSize(3)));
//        UserDto user = new UserDto("trump", "男", 20, "123@test.com", "13345678900");
//        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济");
////        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
//        String json = JsonHelper.getString(rsEventDto);
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/user/list"))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(get("/rs/list"))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$", hasSize(4)));
//        ;
//    }

    @Test
    void should_add_one_rs_event_when_user_not_exists() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        UserDto user = new UserDto("newuser", "男", 20, "123@test.com", "13345678900");
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(4)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(4)));
        ;
    }

    @Test
    void should_return_400_when_user_not_validate() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        UserDto user = new UserDto("newuser", "男", 0, "123@test.com", "13345678900");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济");
        String json = JsonHelper.getString(rsEventDto);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        ;
    }

    @Test
    void should_add_a_header_when_add_one_rs_event() throws Exception {
        UserDto user = new UserDto("newuser", "男", 20, "123@test.com", "13345678900");
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        String json = JsonHelper.getString(rsEventDto);
        MvcResult mvcResult = mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        String index = mvcResult.getResponse().getHeader("index");
        assertEquals("3", index);

    }


    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRespository rsEventRespository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRespository.deleteAll();
    }

    @Test
    void should_add_one_rs_event_to_database_when_user_exists() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        String json = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventEntity> rsEventEntityList = rsEventRespository.findAll();
        assertEquals(1, rsEventEntityList.size());
        assertEquals("猪肉涨价了", rsEventEntityList.get(0).getEventName());
    }


    @Test
    void should_add_one_rs_event_to_database_failed_when_user_doesnot_exists() throws Exception {

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"userId\": 1}";
        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        List<RsEventEntity> rsEventEntityList = rsEventRespository.findAll();
        assertEquals(0, rsEventEntityList.size());
    }

//    @Test
//    void should_delete

    @Test
    void should_get_a_rs_event_with_user() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.user.name", is("newuser")));
    }


//    request: patch /rs/{rsEventId}
//    requestBody: {
//                    “eventName”: “新的热搜事件名”,
//                     “keyword”: “新的关键字”,
//                     “userId”: “user_id”
//    }
//    接口要求：当userId和rsEventId所关联的User匹配时，更新rsEvent信息
//    当userId和rsEventId所关联的User不匹配时，返回400
//            userId为必传字段
//    当只传了eventName没传keyword时只更新eventName
//            当只传了keyword没传eventName时只更新keyword

    @Test
    void should_patch_rsevent_when_user_matches() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
        String json = "{\"eventName\":\"新的热搜事件名\",\"keyWord\":\"新的关键字\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("新的热搜事件名")))
                .andExpect(jsonPath("$.keyWord", is("新的关键字")));
    }

    @Test
    void should_return_400_when_user_not_matches() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
        String json = "{\"eventName\":\"新的热搜事件名\",\"keyWord\":\"新的关键字\",\"userId\": " + userEntity.getId() + 1 + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isBadRequest()));
    }

    @Test
    void should_update_eventName_when_parms_only_contain_this() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
        String json = "{\"eventName\":\"新的热搜事件名\",\"keyWord\":\"\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("新的热搜事件名")))
                .andExpect(jsonPath("$.keyWord", is("key")));
        json = "{\"eventName\":\"新的热搜事件名2\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("新的热搜事件名2")))
                .andExpect(jsonPath("$.keyWord", is("key")));
    }

    @Test
    void should_update_keyWord_when_parms_only_contain_this() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
        String json = "{\"eventName\":\"\",\"keyWord\":\"新的keyWord\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("新的keyWord")));
        json = "{\"keyWord\":\"新的keyWord2\",\"userId\": " + userEntity.getId() + "}";
        mockMvc.perform(patch("/rs/{id}", rsEventEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/{id}", rsEventEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("新的keyWord2")));
    }

}