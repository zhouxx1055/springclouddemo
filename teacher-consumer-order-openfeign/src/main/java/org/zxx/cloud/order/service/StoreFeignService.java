package org.zxx.cloud.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "nacos-store-provider")
public interface StoreFeignService {

    @GetMapping(value = "/payment/nacos/store/{id}")
    String getPayment(@PathVariable("id") Integer id);

}
