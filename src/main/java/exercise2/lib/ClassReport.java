package exercise2.lib;

import java.util.ArrayList;
import java.util.List;

public class ClassReport {

    private String className;
    private String classPackage;
    private final List<Method> methods;
    private final List<Field> fields;

    public ClassReport() {
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public void addMethod(Method method){
        this.methods.add(method);
    }

    public void addField(Field field){
        this.fields.add(field);
    }

    public String getClassName() {
        return className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "ClassReport{" +
                "className='" + className + '\'' + "\n" +
                ", classPackage='" + classPackage + '\'' + "\n" +
                ", methods=" + methods + "\n" +
                ", fields=" + fields + //"\n" +
                '}' + "\n";
    }
}
