package com.thoughtworks.rslist.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyExcption extends RuntimeException {
    private int errorCode;
    private String errorMessage;
}
