package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import exercise2.lib.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncJavaParser {
    private final Vertx vertx;

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

    public Future<PackageReport> getPackageReportNonRecursively(String srcPackagePath){
        return vertx.executeBlocking(p ->{

            PackageReport packageReport = new PackageReport();
            List<String> filePaths = listOfAllFiles(srcPackagePath);
            List<Future> results = new ArrayList<>();

            for (String file : filePaths){
                try {
                    CompilationUnit cu = StaticJavaParser.parse(new File(file));
                    if(cu.getType(0).asClassOrInterfaceDeclaration().isInterface()){
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
                           packageReport.setPackageName(getPackageNameFromPackageReport(packageReport));
                           p.complete(packageReport);
                    });
        });
    }

    private String getPackageNameFromPackageReport(PackageReport packageReport){
        String pkg = "Not Found";
        if(!packageReport.getClassReports().isEmpty()){
            pkg = packageReport.getClassReports().get(0).getClassPackage();
        }else if(!packageReport.getInterfaceReports().isEmpty()){
            pkg = packageReport.getInterfaceReports().get(0).getInterfacePackage();
        }
        return pkg;
    }

    private List<String> listOfAllFiles(String srcPackagePath){
        return Stream.of(new File(srcPackagePath).listFiles())
                .filter(File::isFile)
                .map(File::toString)
                .collect(Collectors.toList());
    }

    private List<String> listOfAllDirectories(String parentPath){
        File parentFile = new File(parentPath);
        Stream<String> directories = Stream.of(new File(parentPath).listFiles())
                .filter(File::isDirectory)
                .map(File::getPath)
                .map(String::listOfAllDirectories());

    }

    public Future<ProjectReport> getProjectReportNonRecursively(String srcProjectPath) {
        return vertx.executeBlocking(p ->{
            ProjectReport projectReport = new ProjectReport();
            List<String> filePaths = listOfAllFiles(srcProjectPath);
            List<Future> results = new ArrayList<>();

            for (String filePath : filePaths){
                try {
                    File file = new File(filePath);
                    if (file.isDirectory()){
                        results.add(this.getPackageReportNonRecursively(filePath));
                    } else {
                        CompilationUnit cu = StaticJavaParser.parse(file);
                        TypeDeclaration<?> type = cu.getType(0);
                        if (type.asClassOrInterfaceDeclaration().isInterface()){
                            results.add(this.getInterfaceReport(filePath));
                        } else {
                            results.add(this.getClassReport(filePath));
                        }
                    }
                } catch (Exception e) {
                    p.fail(e.getMessage());
                }
            }

        });
    }
}
