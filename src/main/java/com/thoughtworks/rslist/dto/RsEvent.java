package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RsEvent {


    public RsEvent() {
    }

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    private String eventName;
    private String keyWord;
}
