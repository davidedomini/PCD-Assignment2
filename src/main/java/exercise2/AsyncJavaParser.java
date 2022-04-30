package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.utils.SourceRoot;
import exercise2.lib.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
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
            InterfaceReport result = new InterfaceReport();
            InterfaceVisitor analyzer = new InterfaceVisitor(result);
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
            ClassReport result = new ClassReport();
            ClassVisitor analyzer = new ClassVisitor(result);
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

            List<String> filePaths = Stream.of(new File(srcPackagePath).listFiles())
                    .filter(File::isFile)
                    .map(File::toString)
                    .collect(Collectors.toList());

            //TODO: we must understand how to reuse pre-implemented methods to get class/interface reports
            for (String file : filePaths){
                try {
                    CompilationUnit cu = StaticJavaParser.parse(new File(file));
                    if(cu.getType(0).asClassOrInterfaceDeclaration().isInterface()){

                        InterfaceReport result = new InterfaceReport();
                        InterfaceVisitor analyzer = new InterfaceVisitor(result);
                        try {
                            CompilationUnit cu2 = StaticJavaParser.parse(new File(file));
                            analyzer.visit(cu2, null);
                            packageReport.addInterfaceReport(analyzer.getReport());
                        } catch (FileNotFoundException e) {
                           e.printStackTrace();
                        }
                    } else{
                        ClassReport result = new ClassReport();
                        ClassVisitor analyzer = new ClassVisitor(result);
                        try {
                            CompilationUnit cu2 = StaticJavaParser.parse(new File(file));
                            analyzer.visit(cu2, null);
                            packageReport.addClassReport(analyzer.getReport());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    p.fail(e.getMessage());
                }

                if(!packageReport.getClassReports().isEmpty()){
                    String pkg = packageReport.getClassReports().get(0).getClassPackage();
                    packageReport.setPackageName(pkg);
                }else if(!packageReport.getInterfaceReports().isEmpty()){
                    String pkg = packageReport.getInterfaceReports().get(0).getInterfacePackage();
                    packageReport.setPackageName(pkg);
                }else{
                    packageReport.setPackageName("Not found");
                }

                p.complete(packageReport);
            }
        });
    }

    public Future<PackageReport> getPackageReport(String srcPackagePath){
        //TODO: settare package name
        return vertx.executeBlocking(p ->{
            PackageReport result = new PackageReport();
            try {
                SourceRoot sr = new SourceRoot(Paths.get(srcPackagePath));
                sr.tryToParse();
                List<CompilationUnit> cus = sr.getCompilationUnits();
                for (CompilationUnit cu : cus) {
                    ClassOrInterfaceDeclaration d = cu.getType(0).asClassOrInterfaceDeclaration();
                    if(d.isInterface()){
                        System.out.println("sono interfaccia");
                        getInterfaceReport(srcPackagePath + "/" + d.getNameAsString() + ".java")
                                .onSuccess(result::addInterfaceReport)
                                .onFailure((Throwable th)-> {
                                    System.out.println("Returned error: " + th.getMessage());
                                });
                    } else {
                        System.out.println("sono classe");
                        getClassReport(srcPackagePath + "/" + d.getNameAsString() + ".java")
                                .onSuccess(result::addClassReport)
                                .onFailure((Throwable th)-> {
                                    System.out.println("Returned error: " + th.getMessage());
                                });
                    }
                }
                p.complete(result);
            } catch (Exception e) {
                p.fail(e.getMessage());
            }
        });
    }
}
