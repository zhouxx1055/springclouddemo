package org.zxx.cloud.order.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zxx.cloud.order.service.OrderService;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource(name = "restTemplate3")
    private RestTemplate restTemplate3;
    @Resource(name = "restTemplate4")
    private RestTemplate restTemplate4;
    @Value("${service-url.nacos-user-service}")
    private String serverURL;
    @Value("${service-url.nacos-store-service}")
    private String storeServerURL;

    @Override
    public String add(Long id) {
        String orderResult=order(id);
        String storeResult=store(id);

        return orderResult+"\r\n<br/>"+storeResult;
    }
    private String order(Long id){

        return restTemplate3.getForObject(serverURL + "/payment/nacos/" + id, String.class);

    }
    private String store(Long id){
        return  restTemplate4.getForObject(storeServerURL + "/payment/nacos/store/" + id, String.class);
    }
}
