package exercise2;

import exercise2.lib.ProjectReport;

public class Model {

    private ProjectReport report;

    public Model(){
        report = new ProjectReport();
    }

    public void reset(){
        report = new ProjectReport();
    }

    public ProjectReport getReport(){
        return this.report;
    }
}
