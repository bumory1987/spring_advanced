package hello.proxy.pureproxy.decorate.code;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{
    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("Message Decorator 실행");
        String message = component.operation();
        String decoResult = "*****" + message + "*****";
        log.info("Message Decorator before ={} , after = {}" , message ,decoResult);
        return decoResult;
    }
}
