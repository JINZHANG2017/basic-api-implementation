package com.thoughtworks.rslist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserDto {
//    名称(不超过8位字符，不能为空)
//    性别（不能为空）
//    年龄（18到100岁之间，不能为空）
//    邮箱（符合邮箱规范）
//    手机号（1开头的11位数字，不能为空）

    public UserDto(@NotEmpty String name, @NotEmpty String gender, @NotEmpty Integer age, String email, @NotEmpty String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    @NotEmpty
    @Size(max = 8)
    private String name;
    @NotEmpty
    private String gender;
    @NotNull
    @Max(100)
    private Integer age;
    private String email;
    @NotEmpty
    private String phone;
}
