package exercise2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerProject extends AbstractVerticle {

    private AsyncJavaParser lib;
    private String srcDirectory;
    private String topic;
    private List<String> allDirectories;
    private boolean stopFlag;
    private EventBus bus;

    public AnalyzerProject(String srcDirectory, String topic, AsyncJavaParser lib) {
        this.srcDirectory = srcDirectory;
        this.topic = topic;
        this.lib = lib;
        this.stopFlag = false;
    }

    public void start(){
        this.bus = this.getVertx().eventBus();
        bus.consumer("stopMessage", message -> {
            stopFlag = true;
            System.out.println("AnalyzerProject: received stop");
        });
        allDirectories = new ArrayList<>(lib.getAllDirectories(srcDirectory));
        analyzeDirectories(allDirectories);

    }

    private void analyzeDirectories(List<String> srcDirectories){
        if(!stopFlag && !srcDirectories.isEmpty()){
            String pkg = srcDirectories.get(0);
            srcDirectories.remove(0);
            List<String> files = lib.listOfAllFiles(pkg);

            analyzeFiles(files, true);
            analyzeDirectories(srcDirectories);
        }
    }

    private void analyzeFiles(List<String> srcFiles, boolean printPackage){
        if(!stopFlag && !srcFiles.isEmpty()){
            String file = srcFiles.get(0);
            System.out.println(file);
            srcFiles.remove(0);
            //analyze
            try {
                if(lib.isInterface(file)){
                    lib.getInterfaceReport(file)
                            .onSuccess(r -> {
                                if(printPackage) bus.publish(topic, "\nNew package founded: " + r.getInterfacePackage() + "\n");
                                bus.publish(topic, r.toString());
                            });
                }else{
                    lib.getClassReport(file)
                            .onSuccess(r ->{
                                if(printPackage) bus.publish(topic, "\nNew package founded: " + r.getClassPackage());
                                bus.publish(topic, r.toString());
                            });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            analyzeFiles(srcFiles, false);
        }
    }

}
