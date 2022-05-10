package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.PackageReport;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.io.File;
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
    private Model model;

    public AnalyzerProject(String srcDirectory, String topic, AsyncJavaParser lib, Model model) {
        this.srcDirectory = srcDirectory;
        this.topic = topic;
        this.lib = lib;
        this.stopFlag = false;
        this.model = model;
    }

    public void start(){
        this.bus = this.getVertx().eventBus();
        bus.publish(topic, srcDirectory);
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
            PackageReport pkgReport = new PackageReport();
            model.addPackageReport(pkgReport);

            List<String> files = lib.listOfAllFiles(pkg);
            analyzeFiles(files, pkgReport);
            //analyze
            bus.publish(topic, "New package analyzed");
            analyzeDirectories(srcDirectories);
        }
    }

    private void analyzeFiles(List<String> srcFiles, PackageReport myPackage){
        if(!stopFlag && !srcFiles.isEmpty()){
            String file = srcFiles.get(0);
            srcFiles.remove(0);
            //analyze
            try {
                if(lib.isInterface(file)){
                    lib.getInterfaceReport(file)
                            .onSuccess(r -> {
                                myPackage.addInterfaceReport(r);
                                if(myPackage.getPackageName() == null) myPackage.setPackageName(r.getInterfacePackage());
                                //bus.publish(topic, "New interface analyzed");
                                bus.publish(topic, r.toString());
                            });
                }else{
                    lib.getClassReport(file)
                            .onSuccess(r ->{
                                myPackage.addClassReport(r);
                                if(myPackage.getPackageName() == null) myPackage.setPackageName(r.getClassPackage());
                                //bus.publish(topic, "New class analyzed");
                                bus.publish(topic, r.toString());
                            });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            analyzeFiles(srcFiles, myPackage);
        }
    }

}
