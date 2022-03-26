package org.zxx.cloud.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zxx.cloud.order.service.OrderService;
import org.zxx.cloud.order.service.PaymentFeignService;
import org.zxx.cloud.order.service.StoreFeignService;

import javax.annotation.Resource;

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
    private LoadBalancerClient loadBalancerClient;

    @Resource
    private PaymentFeignService paymentFeignService;
    @Resource
    private StoreFeignService storeFeignService;

    /**
     * feign.FeignException$NotFound: status 404 reading PaymentFeignService#getPayment(Integer)
     */

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


    @GetMapping(value = "/payment/nacos/feign/{id}")
    public String oder3(@PathVariable("id") Integer id) {

        String orderResult = null;
        String storeResult = null;
        try {
            orderResult = paymentFeignService.getPayment(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            storeResult = storeFeignService.getPayment(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(orderResult);
        log.info(storeResult);

        String result = "nacos registry(下单成功), serverPort: " + serverPort + "\t id" + id;
        log.info(result);

        return orderResult + "\r\n<br/>" + storeResult + "<br/>" + result;
    }

}
