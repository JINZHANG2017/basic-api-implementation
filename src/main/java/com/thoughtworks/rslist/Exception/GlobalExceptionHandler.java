package com.thoughtworks.rslist.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handle(Exception ex){
        if(ex instanceof MyExcption){
            MyExcption myEx=(MyExcption)ex;
            return ResponseEntity.status(myEx.getErrorCode()).body(myEx.getErrorMessage());
        }else{
            return ResponseEntity.status(500).body("server error");
        }
    }
}
