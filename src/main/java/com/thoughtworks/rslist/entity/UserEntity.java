package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String email;
    private String phone;
    private int voteNum;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventEntityList;

    public UserDto toUserDto(){
        return UserDto.builder()
                .name(name)
                .gender(gender)
                .age(age)
                .email(email)
                .phone(phone).build();
    }
}
