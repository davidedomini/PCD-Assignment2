package common;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utility {

    public List<String> listOfAllFiles(String srcPackagePath){
        return Stream.of(new File(srcPackagePath).listFiles())
                .filter(File::isFile)
                .map(File::toString)
                .collect(Collectors.toList());
    }

    public boolean isInterface(String srcFile) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new File(srcFile));
        return cu.getType(0).asClassOrInterfaceDeclaration().isInterface();
    }

    public String getPackageNameFromPackageReport(PackageReport packageReport){
        String pkg = "Not Found";
        if(!packageReport.getClassReports().isEmpty()){
            pkg = packageReport.getClassReports().get(0).getClassPackage();
        }else if(!packageReport.getInterfaceReports().isEmpty()){
            pkg = packageReport.getInterfaceReports().get(0).getInterfacePackage();
        }
        return pkg;
    }

    public Set<String> getAllDirectories(String srcPath){
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

    public String findMainClass(ProjectReport pr){
        List<String> mainClass = pr.getReports()
                .stream()
                .map(PackageReport::getClassReports)
                .flatMap(Collection::stream)
                .filter(this::hasMainMethod)
                .map(ClassReport::getClassName)
                .collect(Collectors.toList()) ;
        return mainClass.size() > 0 ? mainClass.get(0) : "No main class founded";
    }

    public boolean hasMainMethod(ClassReport cr){
        return cr.getMethods()
                .stream()
                .anyMatch(m -> m.getName().equals("main"));
    }


}
