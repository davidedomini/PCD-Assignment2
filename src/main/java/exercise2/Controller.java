package exercise2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Controller extends AbstractVerticle {

    AsyncJavaParser lib;

    public Controller(AsyncJavaParser lib) {
        this.lib = lib;
    }

    public void start(){
        EventBus bus = this.getVertx().eventBus();
        bus.consumer("my-topic", message -> {
            System.out.println(message.body());
        });
    }

    public void startAnalysis(String srcDirectory){
        lib.analyzeProject(srcDirectory, "my-topic");
    }

    public void stopAnalysis(){

    }

}
