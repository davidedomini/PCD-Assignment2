package exercise2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class AnalyzerProject extends AbstractVerticle {

    private String srcDirectory;
    private String topic;

    public AnalyzerProject(String srcDirectory, String topic) {
        this.srcDirectory = srcDirectory;
        this.topic = topic;
    }

    public void start(){
        EventBus bus = this.getVertx().eventBus();
        bus.publish(topic, srcDirectory);
    }

}
