package exercise2;

import common.Utility;
import common.Flag;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerProject extends AbstractVerticle {

    private AsyncJavaParser lib;
    private Utility utilities = new Utility();
    private String srcDirectory;
    private String topic;
    private List<String> allDirectories;
    private Flag stopFlag;
    private EventBus bus;
    private int analyzedFiles = 0;

    public AnalyzerProject(String srcDirectory, String topic, AsyncJavaParser lib, Flag stopFlag) {
        this.srcDirectory = srcDirectory;
        this.topic = topic;
        this.lib = lib;
        this.stopFlag = stopFlag;
    }

    public void start(){
        this.bus = this.getVertx().eventBus();
        allDirectories = new ArrayList<>(utilities.getAllDirectories(srcDirectory));
        analyzeDirectories(allDirectories);

        System.out.println("Analyzed files: " + this.analyzedFiles);
        try {
            System.out.println("Analisi finita stoppo il verticle");
            vertx.undeploy(this.deploymentID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analyzeDirectories(List<String> srcDirectories){
        if(!stopFlag.isSet() && !srcDirectories.isEmpty()){
            String pkg = srcDirectories.get(0);
            srcDirectories.remove(0);
            List<String> files = utilities.listOfAllFiles(pkg);

            analyzeFiles(files, true);
            analyzeDirectories(srcDirectories);
        }
    }

    private void analyzeFiles(List<String> srcFiles, boolean printPackage){
        if(!stopFlag.isSet() && !srcFiles.isEmpty()){
            this.analyzedFiles++;
            String file = srcFiles.get(0);
            srcFiles.remove(0);
            //analyze
            try {
                if(utilities.isInterface(file)){
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            analyzeFiles(srcFiles, false);
        }
    }

}
