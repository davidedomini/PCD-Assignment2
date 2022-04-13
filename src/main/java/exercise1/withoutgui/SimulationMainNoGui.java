package exercise1.withoutgui;

import exercise1.lib.SimulationController;
import exercise1.lib.SimulationModel;
import exercise1.lib.Boundary;

public class SimulationMainNoGui {
    public static void main(String[] args){
        Boundary bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        long totalIter = 10000;
        int nBodies = 1000;
        SimulationModel simModel = new SimulationModel(nBodies, bounds, totalIter);
        int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println("CPU: " + nWorkers);
        SimulationController simController = new SimulationController(simModel, nWorkers);
        System.out.println("Starting Simulation......");
        simController.execute();
    }
}
