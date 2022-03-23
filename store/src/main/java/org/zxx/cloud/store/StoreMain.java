package org.zxx.cloud.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StoreMain {
    public static void main(String[] args) {
        SpringApplication.run(StoreMain.class, args);
        System.out.println("Store:启动成功");
    }
}
