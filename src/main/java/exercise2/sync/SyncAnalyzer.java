package exercise2.sync;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

public class SyncAnalyzer {

    public InterfaceReport getInterfaceReport(String srcInterfacePath){
        InterfaceReport result = new InterfaceReport();
        InterfaceAnalyzer analyzer = new InterfaceAnalyzer(result);
        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(srcInterfacePath));
            analyzer.visit(cu, null);
            result = analyzer.getReport();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

}
