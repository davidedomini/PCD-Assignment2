package exercise2;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import exercise2.lib.InterfaceReport;
import exercise2.lib.InterfaceVisitor;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.io.File;
import java.io.FileNotFoundException;

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
}
