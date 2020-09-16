package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.util.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @Test
    void should_get_rslist() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
    }

    @Test
    void should_get_one_rs() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));
        mockMvc.perform(get("/rs/2"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));
        mockMvc.perform(get("/rs/3"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")));
    }

    @Test
    void should_get_range_rs() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
        UserDto user=new UserDto("trump","男",20,"123@test.com","1334567890");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")))
        ;
    }

    @Test
    void should_put_a_rs_eventName_is_null() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
        RsEvent rsEvent = new RsEvent(null, "政治");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform((put("/rs/event?id=1")).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("政治")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

    }

    @Test
    void should_put_a_rs_keyWord_is_null() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
        ObjectMapper objectMapper=new ObjectMapper();
        RsEvent rsEvent2 = new RsEvent("第二条事件2", null);
        String json2 = objectMapper.writeValueAsString(rsEvent2);
        mockMvc.perform((put("/rs/event?id=2")).content(json2).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件2")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

    }

    @Test
    void should_put_a_rs3() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
        ObjectMapper objectMapper=new ObjectMapper();
        RsEvent rsEvent3=new RsEvent("第三条事件3","情感");
        String json3 = objectMapper.writeValueAsString(rsEvent3);
        mockMvc.perform((put("/rs/event?id=3")).content(json3).contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件3")))
                .andExpect(jsonPath("$[2].keyWord", is("情感")));

    }

    @Test
    void should_delete_one_rs() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
        mockMvc.perform((delete("/rs/event?id=1")))
                .andExpect((status().isOk()));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")));
    }

    @Test
    void should_add_one_rs_event_when_user_exists() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        UserDto user=new UserDto("trump","男",20,"123@test.com","1334567890");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        String json = JsonHelper.getString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(4)));
        ;
    }

    @Test
    void should_add_one_rs_event_when_user_not_exists() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        mockMvc.perform(get("/rs/list"))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", hasSize(3)));
        UserDto user=new UserDto("newuser","男",20,"123@test.com","1334567890");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        String json = JsonHelper.getString(rsEvent);
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
        UserDto user=new UserDto("newuser","男",0,"123@test.com","1334567890");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        String json = JsonHelper.getString(rsEvent);
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

        UserDto user=new UserDto("newuser","男",20,"123@test.com","1334567890");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        MvcResult mvcResult=mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        String index=mvcResult.getResponse().getHeader("index");
        assertEquals("3",index);

    }

    @Test
    void should_return_400_when_range_not_legal() throws Exception {
        mockMvc.perform(get("/rs/list?start=-1&end=3"))
                .andExpect((status().isBadRequest()));
    }
}