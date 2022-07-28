package hello.proxy.config.v1_proxy;


import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace){
        OrderRepositoryV2 repositoryImpl = new OrderRepositoryConcreteProxy( new OrderRepositoryV2(), logTrace);
        return repositoryImpl;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace){
        OrderServiceV2 serviceImpl = new OrderServiceConcreteProxy( new OrderServiceV2(orderRepositoryV2(logTrace) ) , logTrace);
        return serviceImpl;
    }


    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace){
        OrderControllerV2 controllerV2 = new OrderControllerConcreteProxy( new OrderControllerV2(orderServiceV2(logTrace) ) , logTrace);
        return controllerV2;
    }


}
