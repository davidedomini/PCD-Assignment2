package exercise2.lib;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class PackageVisitor extends VoidVisitorAdapter {

    private PackageReport pr;

    public PackageVisitor(PackageReport pr) {
        this.pr = pr;
    }

    public PackageReport getReport() {
        return pr;
    }
}
