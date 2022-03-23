package org.zxx.cloud.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/store")
@Slf4j
public class Store {

    @Value("${spring.application.name}")
    private String serverName;
    @Value("${server.port}")
    private Long serverPort;

    
    @GetMapping("/test")
    public String test(@RequestParam String name){

        return Thread.currentThread().getName()+" Store: Hi "+name;
    }

    @GetMapping("/add")
    public String test2(@RequestParam String name){
        log.info(serverName+serverPort+"开始库存。。。");
        log.info(serverName+serverPort+"库存。。。");
        log.info(serverName+serverPort+"完成库存。。。");
        String result=serverName+serverPort+"("+Thread.currentThread().getName()+")"+name+" 库存成功 ";
        return result;
    }
    
}
