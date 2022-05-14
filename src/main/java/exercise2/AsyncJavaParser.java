package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import common.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class AsyncJavaParser {

    private final Vertx vertx;
    private Utility utilities = new Utility();

    public AsyncJavaParser(Vertx vertx){
        this.vertx = vertx;
    }

    public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath){
        return vertx.executeBlocking(p ->{
            InterfaceVisitor analyzer = new InterfaceVisitor();
            try {
                CompilationUnit cu = StaticJavaParser.parse(new File(srcInterfacePath));
                analyzer.visit(cu, null);
                p.complete(analyzer.getReport());
            } catch (FileNotFoundException e) {
                p.fail(e.getMessage());
            }
        });
    }

    public Future<ClassReport> getClassReport(String srcClassPath){
        return vertx.executeBlocking(p ->{
            ClassVisitor analyzer = new ClassVisitor();
            try {
                CompilationUnit cu = StaticJavaParser.parse(new File(srcClassPath));
                analyzer.visit(cu, null);
                p.complete(analyzer.getReport());
            } catch (FileNotFoundException e) {
                p.fail(e.getMessage());
            }
        });
    }

    public Future<PackageReport> getPackageReport(String srcPackagePath){
        return vertx.executeBlocking(p ->{

            PackageReport packageReport = new PackageReport();
            List<String> filePaths = utilities.listOfAllFiles(srcPackagePath);
            List<Future> results = new ArrayList<>();

            for (String file : filePaths){
                try {
                    if(utilities.isInterface(file)){
                        results.add(this.getInterfaceReport(file));
                    } else{
                        results.add(this.getClassReport(file));
                    }
                } catch (Exception e) {
                    p.fail(e.getMessage());
                }
            }
            CompositeFuture
                    .all(results)
                    .onSuccess((CompositeFuture res) -> {
                           res.result().list()
                                    .forEach(i -> {
                                        if (i instanceof ClassReport){
                                            packageReport.addClassReport((ClassReport) i);
                                        }else{
                                            packageReport.addInterfaceReport((InterfaceReport) i);
                                        }
                                    });
                           packageReport.setPackageName(utilities.getPackageNameFromPackageReport(packageReport));
                           p.complete(packageReport);
                    });
        });
    }

    public Future<ProjectReport> getProjectReport(String scrProjectPath){
        return vertx.executeBlocking(p ->{
            ProjectReport pr = new ProjectReport();
            Set<String> packages = utilities.getAllDirectories(scrProjectPath);
            List<Future> results = packages.stream()
                    .map(this::getPackageReport)
                    .collect(Collectors.toList());
            CompositeFuture
                    .all(results)
                    .onSuccess((CompositeFuture res) ->{
                        res.result().list()
                                .forEach(e -> pr.addReport((PackageReport) e));
                        pr.setMainClass(utilities.findMainClass(pr));
                        p.complete(pr);
                    });
        });
    }

    public void analyzeProject(String srcProject, String topic, Flag stopFlag){
        vertx.deployVerticle(new AnalyzerProject(srcProject, topic, this, stopFlag));
    }
}
