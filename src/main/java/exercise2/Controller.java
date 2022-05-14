package exercise2;

import common.ControllerInterface;
import common.Flag;
import common.View;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Controller extends AbstractVerticle implements ControllerInterface {

    AsyncJavaParser lib;
    EventBus bus;
    View view;
    Flag stopFlag;

    public Controller(AsyncJavaParser lib) {
        this.lib = lib;
        this.stopFlag = new Flag();
    }

    public void setView(View view) {
        this.view = view;
    }

    public void start(){
        bus = this.getVertx().eventBus();
        bus.consumer("updatesAnalysis", message -> view.notifyUpdates(message.body().toString()));
    }

    public void startAnalysis(String srcDirectory){
        this.stopFlag.reset();
        lib.analyzeProject(srcDirectory, "updatesAnalysis", stopFlag);
    }

    public void stopAnalysis(){
        stopFlag.stop();
    }

}
