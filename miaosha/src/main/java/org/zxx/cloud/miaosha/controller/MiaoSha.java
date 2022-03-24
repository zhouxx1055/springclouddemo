package org.zxx.cloud.miaosha.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/miaosha")
@Slf4j
public class MiaoSha {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.nacos-order-service}")
    private String oderServerURL;
    @Value("${service-url.nacos-store-service}")
    private String storeServerURL;
    @Value("${spring.application.name}")
    private String serverName;
    @Value("${server.port}")
    private Long serverPort;

    public MiaoSha(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    public String test(@RequestParam String name) {
        log.info(serverName + serverPort + "开始秒杀。。。");
        log.info(serverName + serverPort + "秒杀。。。");

        try {
            String orderResult = restTemplate.getForObject(oderServerURL + "/api/v1/order/add?name=" + name, String.class);
            log.info(orderResult);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            String storeResult = restTemplate.getForObject(storeServerURL + "/api/v1/store/add?name=" + name, String.class);
            log.info(storeResult);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info(serverName + serverPort + "完成秒杀。。。");
        return serverName + serverPort + Thread.currentThread().getName() + " MiaoSha: Hi " + name;
    }
}
