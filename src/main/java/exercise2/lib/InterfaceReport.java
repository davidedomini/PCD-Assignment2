package exercise2.lib;

import exercise2.lib.Method;

import java.util.ArrayList;
import java.util.List;

public class InterfaceReport{

    private String interfaceName;
    private String interfacePackage;
    private final List<Method> methods;

    public InterfaceReport(){
        this.methods = new ArrayList<>();
    }

    public void addMethod(Method method){
        this.methods.add(method);
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setInterfacePackage(String interfacePath) {
        this.interfacePackage = interfacePath;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getInterfacePackage() {
        return interfacePackage;
    }

    public List<Method> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "InterfaceReport{" +
                "interfaceName='" + interfaceName + '\'' +
                ", interfacePackage='" + interfacePackage + '\'' +
                ", methods=" + methods +
                '}';
    }
}

