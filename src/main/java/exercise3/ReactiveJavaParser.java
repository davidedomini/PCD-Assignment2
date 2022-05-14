package exercise3;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import common.*;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReactiveJavaParser {

    private Utility utilities = new Utility();

    Flowable<InterfaceReport> getInterfaceReport(String path) {
        return Flowable.fromCallable(() -> analyzeInterface(path));
    }

    private InterfaceReport analyzeInterface(String path){
        InterfaceVisitor analyzer = new InterfaceVisitor();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        analyzer.visit(cu, null);
        return analyzer.getReport();
    }

    Flowable<ClassReport> getClassReport(String path) {
        return Flowable.fromCallable(() -> analyzeClass(path));
    }

    private ClassReport analyzeClass(String path){
        ClassVisitor analyzer = new ClassVisitor();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        analyzer.visit(cu, null);
        return analyzer.getReport();
    }

    Flowable<PackageReport> getPackageReport(String path){
        return Flowable.fromCallable(() -> {
            List<String> files = utilities.listOfAllFiles(path);
            PackageReport result = new PackageReport();

            for(String f : files){
                if(utilities.isInterface(f)){
                    getInterfaceReport(f).subscribe(result::addInterfaceReport);
                }else{
                    getClassReport(f).subscribe(result::addClassReport);
                }
            }
            result.setPackageName(utilities.getPackageNameFromPackageReport(result));
            return result;
        });
    }

    Flowable<ProjectReport> getProjectReport(String path){
        return Flowable.fromCallable(() ->{
            List<String> packages = new ArrayList<>(utilities.getAllDirectories(path));
            ProjectReport result = new ProjectReport();

            for(String p : packages){
                getPackageReport(p).subscribe(result::addReport);
            }
            result.setMainClass(utilities.findMainClass(result));
            return result;
        });
    }

    void analyzeProject(String path, PublishSubject<String> streamReports, Flag stopFlag){
        List<String> packages = new ArrayList<>(utilities.getAllDirectories(path));
        boolean printPackage;

        for (String pkg : packages){
            printPackage = true;
            List<String> files = utilities.listOfAllFiles(pkg);
            for (String f : files){
                if(!stopFlag.isSet()){
                    if(utilities.isInterface(f)){
                        InterfaceReport r = analyzeInterface(f);
                        if (printPackage){
                            streamReports.onNext("New package founded: " + r.getInterfacePackage());
                            printPackage = false;
                        }
                        streamReports.onNext(r.toString());
                    }else{
                        ClassReport r = analyzeClass(f);
                        if (printPackage){
                            streamReports.onNext("New package founded: " + r.getClassPackage());
                            printPackage = false;
                        }
                        streamReports.onNext(r.toString());
                    }
                }
            }
        }

        streamReports.onComplete();

    }

}
