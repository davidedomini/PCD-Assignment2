package exercise2.lib;

import java.util.ArrayList;
import java.util.List;

public class ProjectReport {

    private String projectName;
    private final List<PackageReport> packageReports;
    private String mainClassname;

    public ProjectReport() {
        this.packageReports = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ProjectReport{" +
                "projectName='" + projectName + '\'' +
                ", packageReports=" + packageReports +
                ", mainClassname='" + mainClassname + '\'' +
                '}';
    }
}
