package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@NoArgsConstructor
public class RsEvent {


    public RsEvent(String eventName, String keyWord, UserDto user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }
//    @JsonIgnore
//    public UserDto getUser() {
//        return user;
//    }
//
//    @JsonProperty
//    public void setUser(UserDto user) {
//        this.user = user;
//    }
    private String eventName;
    private String keyWord;
    private UserDto user;
}
