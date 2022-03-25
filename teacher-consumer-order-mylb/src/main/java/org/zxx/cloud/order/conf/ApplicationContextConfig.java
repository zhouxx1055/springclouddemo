package org.zxx.cloud.order.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Autowired
    private   LoadBalancerClient loadBalancerClient;

    @Bean(name = "restTemplate",value = "restTemplate")
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "restTemplate2",value = "restTemplate2")
    @LoadBalanced
    public RestTemplate getRestTemplate2() {
        return new RestTemplate();
    }

    @Bean(name = "restTemplate3",value = "restTemplate3")
    @LoadBalanced
    public RestTemplate getRestTemplate3() {
        return new RestTemplate();
    }

    @Bean(name = "restTemplate4",value = "restTemplate4")
    @LoadBalanced
    public RestTemplate getRestTemplate4() {
        return new RestTemplate();
    }

    @Bean(name = "serviceInstance",value = "serviceInstance")
    public ServiceInstance getLoadBalancerClient(){
        return loadBalancerClient.choose("nacos-payment-provider");
    }
    @Bean(name = "storeServiceInstance",value = "storeServiceInstance")
    public ServiceInstance getLoadBalancerClient2(){
        return loadBalancerClient.choose("nacos-store-provider");
    }

}
