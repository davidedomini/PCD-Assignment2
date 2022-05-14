package common;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVisitor extends VoidVisitorAdapter<Void> {

    private ClassReport cr;

    public ClassVisitor() {
        this.cr = new ClassReport();
    }

    public ClassReport getReport() {
        return cr;
    }

    public void visit(PackageDeclaration pd, Void collector) {
        super.visit(pd, collector);
        this.cr.setClassPackage(pd.getNameAsString());
    }

    public void visit(ClassOrInterfaceDeclaration id, Void collector) {
        super.visit(id, collector);
        this.cr.setClassName(id.getNameAsString());
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

        this.cr.addMethod(m);
    }

    public void visit(FieldDeclaration fd, Void collector){
        super.visit(fd, collector);
        fd.getVariables()
                .stream()
                .map(f -> new Field(f.getNameAsString(), f.getTypeAsString()))
                .forEach(cr::addField);
    }

}
