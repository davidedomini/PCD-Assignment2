package exercise2.sync;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class InterfaceAnalyzer extends VoidVisitorAdapter<Void> {
    private InterfaceReport ir;
    public InterfaceAnalyzer(InterfaceReport ir){
        this.ir = ir;
    }

    public InterfaceReport getReport(){
        return this.ir;
    }

    public void visit(ClassOrInterfaceDeclaration id, Void collector){
        super.visit(id, collector);
        this.ir.setInterfaceName(id.getNameAsString());
    }

    public void visit(MethodDeclaration md, Void collector) {
        super.visit(md, collector);
        Method m = new Method();
        m.setName(md.getNameAsString());
        m.setType(md.getTypeAsString());
        md.getModifiers()
                .stream()
                .map(Modifier::toString)
                .forEach(m::addModifier);
        this.ir.addMethod(m);
    }

    public void visit(PackageDeclaration fd, Void collector) {
        super.visit(fd, collector);
        this.ir.setInterfacePackage(fd.getNameAsString());
    }
}
