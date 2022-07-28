package hello.proxy.config.v3_factory;


import hello.proxy.app.v1.*;
import hello.proxy.config.v3_factory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {


    private Advisor getAdvisor(LogTrace logTrace){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice logTraceAdvice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, logTraceAdvice);
    }
    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace){
        ProxyFactory proxyFactory = new ProxyFactory(new OrderRepositoryV1Impl());
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryV1 proxy = (OrderRepositoryV1) proxyFactory.getProxy();
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace){
        ProxyFactory proxyFactory = new ProxyFactory(new OrderServiceV1Impl(orderRepositoryV1(logTrace)));
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderServiceV1 proxy = (OrderServiceV1) proxyFactory.getProxy();
        return proxy;
    }


    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace){
        ProxyFactory proxyFactory = new ProxyFactory(new OrderControllerV1Impl(orderServiceV1(logTrace)));
        proxyFactory.addAdvisor(getAdvisor(logTrace));
        OrderControllerV1 proxy = (OrderControllerV1) proxyFactory.getProxy();
        return proxy;
    }


}
