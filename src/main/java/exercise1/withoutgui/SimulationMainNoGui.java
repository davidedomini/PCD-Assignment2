package exercise1.withoutgui;

import exercise1.SimulationController;
import exercise1.SimulationModel;
import exercise1.lib.Boundary;

public class SimulationMainNoGui {
    public static void main(String[] args){
        Boundary bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        long totalIter = 100;
        int nBodies = 100;
        SimulationModel simModel = new SimulationModel(nBodies, bounds, totalIter);
        int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println("CPU: " + nWorkers);
        SimulationController simController = new SimulationController(simModel, nWorkers);
        simController.execute();
    }
}