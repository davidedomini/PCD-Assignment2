package exercise2.lib;

import java.util.ArrayList;
import java.util.List;

public class PackageReport {

    private String packageName;
    private final List<ClassReport> classReports;
    private final List<InterfaceReport> interfaceReports;

    public PackageReport() {
        classReports = new ArrayList<>();
        interfaceReports = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public List<ClassReport> getClassReports() {
        return classReports;
    }

    public List<InterfaceReport> getInterfaceReports() {
        return interfaceReports;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void addClassReport(ClassReport cr){
        this.classReports.add(cr);
    }

    public void addInterfaceReport(InterfaceReport ir){
        this.interfaceReports.add(ir);
    }

    @Override
    public String toString() {
        return "PackageReport{" +
                "packageName='" + packageName + '\'' +
                ", classReports=" + classReports +
                ", interfaceReports=" + interfaceReports +
                '}';
    }
}
