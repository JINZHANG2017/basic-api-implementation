package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsEventDto {



    public RsEventDto(String eventName, String keyWord) {
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
    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;
//    @NotNull
//    @Valid
//    private UserDto user;


    private  UserDto user;

    @JsonProperty
    public UserDto getUser() {
        return user;
    }

    @JsonIgnore
    public void setUser(UserDto user) {
        this.user = user;
    }

    @NotNull
    private Integer userId;

    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }
    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    //加注释之后提交也是null
    //@Valid
//    @JsonIgnore
//    public UserDto getUser() {
//        return user;
//    }
//
//    @JsonProperty
//    public void setUser(UserDto user) {
//        this.user = user;
//    }
}
