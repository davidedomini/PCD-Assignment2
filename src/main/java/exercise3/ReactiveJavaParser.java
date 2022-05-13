package exercise3;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.*;
import io.reactivex.rxjava3.core.Flowable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
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
                    getInterfaceReport(f).subscribe(result::addInterfaceReport);
                }else{
                    getClassReport(f).subscribe(result::addClassReport);
                }
            }
            result.setPackageName(getPackageNameFromPackageReport(result));
            return result;
        });
    }

    Flowable<ProjectReport> getProjectReport(String path){
        return Flowable.fromCallable(() ->{
            List<String> packages = new ArrayList<>(getAllDirectories(path));
            ProjectReport result = new ProjectReport();

            for(String p : packages){
                getPackageReport(p).subscribe(result::addReport);
            }
            result.setMainClass(findMainClass(result));
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

    private Set<String> getAllDirectories(String srcPath){
        Set<String> t = new HashSet<>();
        Set<String> subDir = Stream.of(new File(srcPath).listFiles())
                .filter(File::isDirectory)
                .map(File::toString)
                .collect(Collectors.toSet());

        subDir.stream()
                .map(this::getAllDirectories)
                .forEach(t::addAll);

        subDir.addAll(t);
        subDir.add(srcPath);
        return subDir;
    }

    private String findMainClass(ProjectReport pr){
        List<String> mainClass = pr.getReports()
                .stream()
                .map(PackageReport::getClassReports)
                .flatMap(Collection::stream)
                .filter(this::hasMainMethod)
                .map(ClassReport::getClassName)
                .collect(Collectors.toList()) ;
        return mainClass.size() > 0 ? mainClass.get(0) : "No main class founded";
    }

    private boolean hasMainMethod(ClassReport cr){
        return cr.getMethods()
                .stream()
                .anyMatch(m -> m.getName().equals("main"));
    }

}
