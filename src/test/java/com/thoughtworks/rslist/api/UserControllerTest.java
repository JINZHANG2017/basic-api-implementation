package com.thoughtworks.rslist.api;

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

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void should_return_400_when_name_is_empty() throws Exception {
        UserDto user = new UserDto("", "男", 20, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_name_more_than_8() throws Exception {
        UserDto user = new UserDto("123456789", "男", 20, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_gender_is_empty() throws Exception {
        UserDto user = new UserDto("1234567", "", 20, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_age_is_empty() throws Exception {
        UserDto user = new UserDto("1234567", "男", null, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_age_more_than_100() throws Exception {
        UserDto user = new UserDto("1234567", "男", 110, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_age_less_than_18() throws Exception {
        UserDto user = new UserDto("1234567", "男", 17, "abc@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_email_not_validate() throws Exception {
        UserDto user = new UserDto("1234567", "男", 20, "@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_phone_not_validate() throws Exception {
        UserDto user = new UserDto("1234567", "男", 20, "123@test.com", "11345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_200_when_all_validate() throws Exception {
        UserDto user = new UserDto("1234567", "男", 20, "123@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRespository rsEventRespository;

    @BeforeEach
    void clean_tables(){
        userRepository.deleteAll();
        rsEventRespository.deleteAll();
    }


    @Test
    void should_register_user_save_to_database() throws Exception {
        //to user = new UserDto("1234567", "男", 20, "123@test.com", "13345678900");
//        UserEntity user= UserEntity.builder()
//                .name("1234567")
//                .gender("男")
//                .age(20)
//                .email("123@test.com")
//                .phone("13345678900")
//                .build();
//        userRepository.save(user);
        UserDto user = new UserDto("1234567", "男", 20, "123@test.com", "13345678900");
        String json = JsonHelper.getString(user);
        mockMvc.perform(post("/user/register")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> userList = userRepository.findAll();
        assertEquals(1,userList.size());
        assertEquals("男",userList.get(0).getGender());

    }

    @Test
    void should_get_user_from_database_by_id() throws Exception {
        UserEntity userEntity=UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        mockMvc.perform(get("/user/{id}",userEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("newuser")));
    }

    @Test
    void should_delete_user_from_database_by_id() throws Exception {
        UserEntity userEntity=UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        mockMvc.perform(get("/user/{id}",userEntity.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("newuser")));
        mockMvc.perform(delete("/user/{id}",userEntity.getId()))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/user/{id}",userEntity.getId()))
                .andExpect((status().isBadRequest()));
    }

    @Test
    void should_delete_rsevent_when_delete_user_from_database() throws Exception {
        UserEntity userEntity=UserEntity.builder()
                .name("newuser")
                .age(20)
                .email("1@t.com")
                .gender("男")
                .phone("13345678900").build();
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName("event 0")
                .keyWord("key")
                .user(userEntity)
                .build();
        rsEventRespository.save(rsEventEntity);
//        mockMvc.perform(get("/user/{id}",userEntity.getId()))
//                .andExpect((status().isOk()))
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("newuser")));
        mockMvc.perform(delete("/user/{id}",userEntity.getId()))
                .andExpect((status().isNoContent()));
//        mockMvc.perform(get("/user/{id}",userEntity.getId()))
//                .andExpect((status().isBadRequest()));
        List<UserEntity> userEntityList=userRepository.findAll();
        List<RsEventEntity> rsEventEntityList = rsEventRespository.findAll();

        assertEquals(0,userEntityList.size());
        assertEquals(0,rsEventEntityList.size());
    }
}