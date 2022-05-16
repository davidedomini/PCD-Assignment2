package common;

import java.util.ArrayList;
import java.util.List;

public class ProjectReport {

    private final List<PackageReport> reports;
    private String mainClass;

    public ProjectReport(){
        this.reports = new ArrayList<>();
    }

    public List<PackageReport> getReports() {
        return reports;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String c) {
        this.mainClass = c;
    }

    public void addReport(PackageReport report){
        this.reports.add(report);
    }

    @Override
    public String toString() {
        return "ProjectReport{" + "\n" +
                "MainClass='" + mainClass + '\'' + "\n" +
                ", Reports=" + reports +
                '}' + "\n";
    }
}
