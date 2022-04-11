package exercise1;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MasterService extends Thread{

    private SimulationModel simulationModel;
    private ExecutorService executor;

    public MasterService(SimulationModel simulationModel, int poolSize){
        this.simulationModel = simulationModel;
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void run(){

    }


}
