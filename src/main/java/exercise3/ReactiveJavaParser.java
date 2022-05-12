package exercise3;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.*;
import io.reactivex.rxjava3.core.Flowable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReactiveJavaParser {

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
            List<String> files = listOfAllFiles(path);
            PackageReport result = new PackageReport();

            for(String f : files){
                if(isInterface(f)){
                    InterfaceVisitor analyzer = new InterfaceVisitor();
                    CompilationUnit cu = StaticJavaParser.parse(new File(f));
                    analyzer.visit(cu, null);
                    result.addInterfaceReport(analyzer.getReport());
                }else{
                    ClassVisitor analyzer = new ClassVisitor();
                    CompilationUnit cu = StaticJavaParser.parse(new File(f));
                    analyzer.visit(cu, null);
                    result.addClassReport(analyzer.getReport());
                }
            }
            result.setPackageName(getPackageNameFromPackageReport(result));
            return result;
        });
    }

    private List<String> listOfAllFiles(String srcPackagePath){
        return Stream.of(new File(srcPackagePath).listFiles())
                .filter(File::isFile)
                .map(File::toString)
                .collect(Collectors.toList());
    }

    private boolean isInterface(String srcFile) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(srcFile));
        return cu.getType(0).asClassOrInterfaceDeclaration().isInterface();
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

}
