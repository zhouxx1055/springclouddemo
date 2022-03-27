package org.zxx.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zxx.cloud.module.vo.input.User;
import org.zxx.cloud.order.conf.PassToken;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class Order {

//    @Resource
//    private RestTemplate restTemplate;
    @Value("${spring.application.name}")
    private String serverName;
    @Value("${server.port}")
    private Long serverPort;

    @GetMapping("/test")
    public String test(@RequestParam String name){

        return Thread.currentThread().getName()+" Order: Hi "+name;
    }

//    @GetMapping("/test1")
//    public String test1(@RequestParam String name){
//        String result = restTemplate.getForObject("http://localhost:8002/api/v1/store/test?name=" + name, String.class);
//
//        return Thread.currentThread().getName()+" Order: Hi "+name;
//    }

    @PassToken
    @GetMapping("/add")
    public String add(@RequestParam String name){
        log.info(serverName+serverPort+"开始下单。。。");
        log.info(serverName+serverPort+"下单。。。");
        log.info(serverName+serverPort+"完成下单。。。");
        String result=serverName+serverPort+"("+Thread.currentThread().getName()+")"+name+" 下单成功 ";
        return result;
    }

    @PassToken
    @PostMapping("/add1")
    public String add11(@RequestBody User user){
        log.info(serverName+serverPort+"开始下单。。。");
        log.info(serverName+serverPort+"下单。。。");
        log.info(serverName+serverPort+"完成下单。。。");
        String result=serverName+serverPort+"("+Thread.currentThread().getName()+")"+user.getId()+" 下单成功 ";
        return result;
    }

}
