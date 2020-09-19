package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rsevent")
public class RsEventEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "name")
    private String eventName;
    private String keyWord;
//    private int userId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public RsEventDto toRsEventDto(){
        return RsEventDto.builder().eventName(eventName)
                .keyWord(keyWord)
                .userId(user.getId()).build();
    }
}
