package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PrxoyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 jdk 동적 프록시 사용")
    void interfaceProxy(){
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory =new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy =(ServiceInterface) proxyFactory.getProxy();
        log.info("target ={}", target.getClass());
        log.info("proxy ={}", proxy.getClass());
        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();


    }





    @Test
    @DisplayName("구체 클래스만 있으면 cglib 동적 프록시 사용")
    void cglibProxy(){
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory =new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy =(ConcreteService) proxyFactory.getProxy();
        log.info("target ={}", target.getClass());
        log.info("proxy ={}", proxy.getClass());
        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();


    }




    @Test
    @DisplayName("cglib 강제 ")
    void proxyTargetClass(){
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory =new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy =(ServiceInterface) proxyFactory.getProxy();
        log.info("target ={}", target.getClass());
        log.info("proxy ={}", proxy.getClass());
        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }





}
