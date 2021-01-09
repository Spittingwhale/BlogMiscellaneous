package com.czj.exception.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DefineService {
    @PostConstruct
    public void handleMethon() {
        System.out.println("11111111111111111111111111111111111");
        Integer a = null;
        System.out.println(a.equals(""));
    }

    @Bean
    public void handleMethod() {
        System.out.println("222222222222222222222222222222222222");
        Integer a = null;
        System.out.println(a.equals(""));
    }
}
