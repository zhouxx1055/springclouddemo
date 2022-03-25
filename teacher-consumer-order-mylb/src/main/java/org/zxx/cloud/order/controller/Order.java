package org.zxx.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zxx.cloud.order.conf.MyLoadBalancer;
import org.zxx.cloud.order.service.OrderService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
@RequestMapping("consumer")
public class Order {

//    @Autowired
//    private LoadBalancerClient loadBalancerClient;


    @Value("${server.port}")
    private String serverPort;

    @Resource(name = "serviceInstance")
    private ServiceInstance serviceInstance;
    @Resource(name = "storeServiceInstance")
    private ServiceInstance storeServiceInstance;

    @Autowired
    OrderService orderService;
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    @Resource(name = "restTemplate2")
    private RestTemplate restTemplate2;

    @Value("${service-url.nacos-user-service}")
    private String serverURL;
    @Value("${service-url.nacos-store-service}")
    private String storeServerURL;

    @Autowired
    public LoadBalancerClient loadBalancerClient;

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private MyLoadBalancer myLoadBalancer;


    @GetMapping(value = "/payment/nacos/discovery/{id}")
    public String discovery(@PathVariable("id") Long id) {

        int rand = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        List<ServiceInstance> paymentInstances = discoveryClient.getInstances("nacos-payment-provider");
        List<ServiceInstance> storeInstances = discoveryClient.getInstances("nacos-store-provider");

        if (paymentInstances == null || paymentInstances.isEmpty() || storeInstances == null || storeInstances.isEmpty()) {
            return "没有可用的服务，请检查!";
        }
        int payIndex = rand % paymentInstances.size();
        int storeIndex = rand % storeInstances.size();
        ServiceInstance payServiceInstance = paymentInstances.get(payIndex);
        ServiceInstance storeServiceInstance1 = storeInstances.get(storeIndex);

        log.info(payServiceInstance.getServiceId() + " (" + payServiceInstance.getHost() + ":" + payServiceInstance.getPort() + ")" + "===>Say " + id);
        log.info(storeServiceInstance1.getServiceId() + " (" + storeServiceInstance1.getHost() + ":" + storeServiceInstance1.getPort() + ")" + "===>Say " + id);


        // todo 做不了客户端的负载均衡
        String payUrl = String.format("http://%s:%s", payServiceInstance.getHost(), payServiceInstance.getPort());
        String storeUrl = String.format("http://%s:%s", storeServiceInstance1.getHost(), storeServiceInstance1.getPort());

        RestTemplate restTemplate = new RestTemplate();

        log.info(payServiceInstance.getServiceId() + " (" + payUrl + ")");
        log.info(storeServiceInstance1.getServiceId() + " (" + storeUrl + ")");

        String orderResult = restTemplate.getForObject(payUrl + "/payment/nacos/" + id, String.class);
        String storeResult = restTemplate.getForObject(storeUrl + "/payment/nacos/store/" + id, String.class);
        log.info(orderResult);
        log.info(storeResult);

        String result = "nacos registry(下单成功), serverPort: " + serverPort + "\t id" + id;
        log.info(result);

        return orderResult + "\r\n<br/>" + storeResult + "<br/>" + result;

    }

    @GetMapping(value = "/payment/nacos/discovery2/{id}")
    public String discovery2(@PathVariable("id") Long id) {


        List<ServiceInstance> paymentInstances = discoveryClient.getInstances("nacos-payment-provider");
        List<ServiceInstance> storeInstances = discoveryClient.getInstances("nacos-store-provider");

        if (paymentInstances == null || paymentInstances.isEmpty() || storeInstances == null || storeInstances.isEmpty()) {
            return "没有可用的服务，请检查!";
        }
        log.info("paymentInstances.size:"+paymentInstances.size());
        log.info("paymentInstances.size:"+storeInstances.size());
        ServiceInstance payServiceInstance = myLoadBalancer.instances(paymentInstances);
        ServiceInstance storeServiceInstance1 = myLoadBalancer.instances(storeInstances);

        log.info(payServiceInstance.getServiceId() + " (" + payServiceInstance.getHost() + ":" + payServiceInstance.getPort() + ")" + "===>Say " + id);
        log.info(storeServiceInstance1.getServiceId() + " (" + storeServiceInstance1.getHost() + ":" + storeServiceInstance1.getPort() + ")" + "===>Say " + id);


        // todo 做不了客户端的负载均衡
        String payUrl = String.format("http://%s:%s", payServiceInstance.getHost(), payServiceInstance.getPort());
        String storeUrl = String.format("http://%s:%s", storeServiceInstance1.getHost(), storeServiceInstance1.getPort());

        RestTemplate restTemplate = new RestTemplate();

        log.info(payServiceInstance.getServiceId() + " (" + payUrl + ")");
        log.info(storeServiceInstance1.getServiceId() + " (" + storeUrl + ")");

        String orderResult = restTemplate.getForObject(payUrl + "/payment/nacos/" + id, String.class);
        String storeResult = restTemplate.getForObject(storeUrl + "/payment/nacos/store/" + id, String.class);
        log.info(orderResult);
        log.info(storeResult);

        String result = "nacos registry(下单成功), serverPort: " + serverPort + "\t id" + id;
        log.info(result);

        return orderResult + "\r\n<br/>" + storeResult + "<br/>" + result;

    }


    @GetMapping(value = "/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Long id) {
        RestTemplate restTemplate = new RestTemplate();
        // todo 为啥会覆盖？
        ServiceInstance payment = loadBalancerClient.choose("nacos-payment-provider");
        String payUrl = String.format("http://%s:%s", payment.getHost(), payment.getPort());
        log.info(payment.getServiceId() + " (" + payUrl + ")");
        String orderResult = restTemplate.getForObject(payUrl + "/payment/nacos/store/" + id, String.class);
//        String storeResult= restTemplate2.getForObject(storeServerURL + "/payment/nacos/store/" + id, String.class);
        log.info(orderResult);
//        log.info(storeResult);


        return orderResult;

    }

    @GetMapping(value = "/payment/nacos/test/{id}")
    public String oder(@PathVariable("id") Long id) {

        return orderService.add(id);
    }

    @GetMapping(value = "/payment/nacos/test2/{id}")
    public String oder2(@PathVariable("id") Long id) {

//        ServiceInstance payment= loadBalancerClient.choose("nacos-payment-provider");
//        ServiceInstance store= loadBalancerClient.choose("nacos-store-provider");

//        log.info("====>"+payment.getServiceId() + " (" + payment.getHost() + ":" + payment.getPort() + ")" + "===>Say " + id);
//        log.info("====>"+store.getServiceId() + " (" + store.getHost() + ":" + store.getPort() + ")" + "===>Say " + id);


        log.info(serviceInstance.getServiceId() + " (" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + ")" + "===>Say " + id);
        log.info(storeServiceInstance.getServiceId() + " (" + storeServiceInstance.getHost() + ":" + storeServiceInstance.getPort() + ")" + "===>Say " + id);


        // todo 做不了客户端的负载均衡
        String payUrl = String.format("http://%s:%s", serviceInstance.getHost(), serviceInstance.getPort());
        String storeUrl = String.format("http://%s:%s", storeServiceInstance.getHost(), storeServiceInstance.getPort());

        RestTemplate restTemplate = new RestTemplate();

        log.info(serviceInstance.getServiceId() + " (" + payUrl + ")");
        log.info(storeServiceInstance.getServiceId() + " (" + storeUrl + ")");

        String orderResult = restTemplate.getForObject(payUrl + "/payment/nacos/" + id, String.class);
        String storeResult = restTemplate.getForObject(storeUrl + "/payment/nacos/store/" + id, String.class);
        log.info(orderResult);
        log.info(storeResult);

        String result = "nacos registry(下单成功), serverPort: " + serverPort + "\t id" + id;
        log.info(result);

        return orderResult + "\r\n<br/>" + storeResult + "<br/>" + result;
    }

}
