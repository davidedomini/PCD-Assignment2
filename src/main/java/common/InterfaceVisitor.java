package common;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class InterfaceVisitor extends VoidVisitorAdapter<Void> {
    private InterfaceReport ir;

    public InterfaceVisitor() {
        this.ir = new InterfaceReport();
    }

    public InterfaceReport getReport() {
        return this.ir;
    }

    public void visit(PackageDeclaration pd, Void collector) {
        super.visit(pd, collector);
        this.ir.setInterfacePackage(pd.getNameAsString());
    }

    public void visit(ClassOrInterfaceDeclaration id, Void collector) {
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
        md.getParameters()
                .stream()
                .map(e -> new Parameter(e.getNameAsString(), e.getTypeAsString()))
                .forEach(m::addParameter);
        m.setBeginLine(md.getBegin().get().line);
        m.setEndLine(md.getEnd().get().line);

        this.ir.addMethod(m);
    }
}
