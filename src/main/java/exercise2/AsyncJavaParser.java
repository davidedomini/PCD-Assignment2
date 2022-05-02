package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public void aa(){
        System.out.println(getAllDirectories("src/main/java/exercise2/"));
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

    private Set<String> getAllDirectories(String srcPath){
        Set<String> s = new HashSet<>();
        Set<String> subDir = Stream.of(new File(srcPath).listFiles())
                .filter(File::isDirectory)
                .map(File::toString)
                .collect(Collectors.toSet());

        //Set<String> t = new HashSet<>();
//        subDir.stream()
//            .map(this::getAllDirectories)
//            .forEach(t::addAll);

        for(String d : subDir){
            s.addAll(getAllDirectories(d));
        }
        subDir.addAll(s);
        subDir.add(srcPath);
        return subDir;
    }

}
