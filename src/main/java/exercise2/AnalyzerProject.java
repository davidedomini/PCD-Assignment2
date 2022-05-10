package exercise2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.util.List;

public class AnalyzerProject extends AbstractVerticle {

    private AsyncJavaParser lib;
    private String srcDirectory;
    private String topic;
    private List<String> allDirectories;
    private boolean stopFlag;
    private EventBus bus;
    private Model model;

    public AnalyzerProject(String srcDirectory, String topic, AsyncJavaParser lib, Model model) {
        this.srcDirectory = srcDirectory;
        this.topic = topic;
        this.lib = lib;
        this.stopFlag = false;
        this.bus = this.getVertx().eventBus();
        this.model = model;
    }

    public void start(){
        bus.publish(topic, srcDirectory);
        bus.consumer("stopMessage", message -> {
            stopFlag = true;
            System.out.println("AnalyzerProject: received stop");
        });

        allDirectories = List.copyOf(lib.getAllDirectories(srcDirectory));

        analyzeDirectories(allDirectories);

    }

    private void analyzeDirectories(List<String> srcDirectories){
        if(!stopFlag && !srcDirectories.isEmpty()){
            String pkg = srcDirectories.get(0);
            srcDirectories.remove(0);

            List<String> files = lib.listOfAllFiles(pkg);
            analyzeFiles(files);
            //analyze

            bus.publish(topic, "New package analyzed");
            analyzeDirectories(srcDirectories);
        }
    }

    private void analyzeFiles(List<String> srcFiles){
        if(!stopFlag && !srcFiles.isEmpty()){
            String file = srcFiles.get(0);
            srcFiles.remove(0);
            //analyze

            bus.publish(topic, "New class or interface analyzed");
            analyzeFiles(srcFiles);
        }
    }

}
