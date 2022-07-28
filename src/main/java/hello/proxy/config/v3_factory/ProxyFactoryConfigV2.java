package hello.proxy.config.v3_factory;

import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_factory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class ProxyFactoryConfigV2 {

    private Advisor getAdvisor(LogTrace logTrace){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, logTraceAdvice);
    }
    @Bean
    public OrderRepositoryV2 orderRepositoryV2Man(LogTrace logTrace){
        ProxyFactory proxyFactory = new ProxyFactory(new OrderRepositoryV2());
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryV2 proxy = (OrderRepositoryV2) proxyFactory.getProxy();
        return proxy;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace){
        OrderRepositoryV2 repositoryV2 = orderRepositoryV2Man(logTrace);
        ProxyFactory proxyFactory = new ProxyFactory(new OrderServiceV2(repositoryV2));
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderServiceV2 proxy = (OrderServiceV2) proxyFactory.getProxy();
        return proxy;
    }


    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace){
        ProxyFactory proxyFactory = new ProxyFactory(new OrderControllerV2(orderServiceV2(logTrace)));
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderControllerV2 proxy = (OrderControllerV2) proxyFactory.getProxy();
        return proxy;
    }
}
