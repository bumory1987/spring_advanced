package hello.proxy.postprocess;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class BasicPostProcessTest {

    @Test
    void basicConfig(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicPostProcessConfig.class);

        //A는 빈으로 등록된
        B beanB = applicationContext.getBean("beanA", B.class);
        beanB.hello();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }


    @Slf4j
    @Configuration
    static class BasicPostProcessConfig{
        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        @Bean
        public AToBPostProcessor generator(){
            return new AToBPostProcessor();
        }

    }


    @Slf4j
    static class A{
        public void hello(){
            log.info("hello A");
        }
    }

    @Slf4j
    static class B{
        public void hello(){
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor{

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName = {} bean ={}", beanName, bean);
            if(bean instanceof A){
                return new B();
            }
            return bean;
        }
    }

}
