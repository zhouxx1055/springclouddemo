package org.zxx.cloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OrderMain {

    public static void main(String[] args) {
        SpringApplication.run(OrderMain.class, args);
        System.out.println("启动成功");
    }
}
