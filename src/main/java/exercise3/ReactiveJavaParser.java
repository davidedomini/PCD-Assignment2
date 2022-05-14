package exercise3;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import common.*;
import io.reactivex.rxjava3.core.Flowable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReactiveJavaParser {

    private Utility utilities = new Utility();

    Flowable<InterfaceReport> getInterfaceReport(String path) {
        return Flowable.fromCallable(() -> {
            InterfaceVisitor analyzer = new InterfaceVisitor();
            CompilationUnit cu = StaticJavaParser.parse(new File(path));
            analyzer.visit(cu, null);
            return analyzer.getReport();
        });
    }

    Flowable<ClassReport> getClassReport(String path) {
        return Flowable.fromCallable(() -> {
            ClassVisitor analyzer = new ClassVisitor();
            CompilationUnit cu = StaticJavaParser.parse(new File(path));
            analyzer.visit(cu, null);
            return analyzer.getReport();
        });
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

}
