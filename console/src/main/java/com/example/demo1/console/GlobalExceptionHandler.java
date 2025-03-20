package com.example.demo1.console;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return "系统异常: " + ex.getMessage();
    }
}
