package org.zxx.cloud.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Order {


    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/payment/nacos/store/{id}")
    public String getPayment(@PathVariable("id") Integer id) {
        return "nacos registry(库存成功), serverPort: " + serverPort + "\t id" + id;
    }

}
