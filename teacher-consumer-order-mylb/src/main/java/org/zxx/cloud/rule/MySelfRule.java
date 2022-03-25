package org.zxx.cloud.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RetryRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiaolong
 * @date 2020/12/22 20:54
 * @description 该类是ribbon的自定义策略
 */

@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule() {
        // 此处将ribbon默认使用的轮询策略改为随机策略
//        return new RandomRule();
        return new RandomRule();

    }
}
/**
 *
 * Ribbon 默认的七个策略：
 *
 * BestAvailableRule：选择一个最小的并发请求的Server，逐个考察Server，如果Server被tripped了，则跳过。
 * AvailabilityFilteringRule：过滤掉那些一直连接失败的被标记为circuit tripped的后端Server，
 *      并过滤掉那些高并发的的后端Server或者使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个Server的运行状态。
 * ZoneAvoidanceRule：复合判断Server所在区域的性能和Server的可用性选择Server。
 * RandomRule：随机选择一个Server。
 * RoundRobinRule：轮询选择， 轮询index，选择index对应位置的Server。
 * RetryRule：对选定的负载均衡策略机上重试机制，在一个配置时间段内当选择Server不成功，则一直尝试使用subRule的方式选择一个可用的server。
 * WeightedResponseTimeRule：根据响应时间分配一个weight(权重)，响应时间越长，weight越小，被选中的可能性越低。
 * ResponseTimeWeightedRule：作用同WeightedResponseTimeRule，二者作用是一样的，ResponseTimeWeightedRule后来改名为WeightedResponseTimeRule。
 *
 * */