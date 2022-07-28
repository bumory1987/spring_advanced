package hello.proxy.pureproxy.concreteproxy.code;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic){
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation(){
        log.info("time Decorate 실행");
        long startTime = System.currentTimeMillis();
        String result = concreteLogic.operation();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime= System.currentTimeMillis();
        long cal = endTime - startTime;
        log.info("final time = {}", cal);
        log.info("time Decorate 끝");

        return result;
    }
}
