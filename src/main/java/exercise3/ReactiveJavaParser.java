package exercise3;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.ClassReport;
import exercise2.lib.ClassVisitor;
import exercise2.lib.InterfaceReport;
import exercise2.lib.InterfaceVisitor;
import io.reactivex.rxjava3.core.Flowable;

import java.io.File;

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

}
