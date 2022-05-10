package exercise2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Controller extends AbstractVerticle {

    AsyncJavaParser lib;
    EventBus bus;
    View view;

    public Controller(AsyncJavaParser lib) {
        this.lib = lib;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void start(){
        bus = this.getVertx().eventBus();
        bus.consumer("updatesAnalysis", message -> view.notifyUpdates(message.body().toString()));
    }

    public void startAnalysis(String srcDirectory){
        lib.analyzeProject(srcDirectory, "updatesAnalysis");
    }

    public void stopAnalysis(){
        bus.publish("stopMessage", "Stop analysis");
    }

}
