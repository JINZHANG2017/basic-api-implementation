package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String eventName;
    private String keyWord;
    private UserDto user;
}
