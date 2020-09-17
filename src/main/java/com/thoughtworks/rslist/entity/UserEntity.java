package com.thoughtworks.rslist.entity;

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

    @OneToMany(mappedBy = "userId",cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventEntityList;
}
