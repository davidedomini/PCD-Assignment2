package exercise1;

import exercise1.lib.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MasterService extends Thread{

    private SimulationModel simulationModel;
    private ExecutorService executor;

    public MasterService(SimulationModel simulationModel, int poolSize){
        this.simulationModel = simulationModel;
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void run(){
        simulationModel.init();
        try{
            while(!simulationModel.isCompleted()){
                List<Body> velRes = computeVelocity();
                List<Body> posRes = computePositions(velRes);
                simulationModel.updateVirtualTime();
                simulationModel.update(posRes);
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    private List<Body> computeVelocity(){
        List<Future<Body>> results = new ArrayList<>();
        List<Body> bodiesRes = new ArrayList<>();

        for(Body b: simulationModel.getBodies()){
            try{
                Future<Body> res = executor
                        .submit(new ComputeVelocityTask(new Body(b), simulationModel.getDt(), simulationModel.getBodies()));
                results.add(res);
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }

        for(Future<Body> b : results){
            try{
                bodiesRes.add(b.get());
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return bodiesRes;
    }

    private List<Body> computePositions(List<Body> bodies){
        List<Future<Body>> results = new ArrayList<>();
        List<Body> bodiesRes = new ArrayList<>();

        for(Body b: bodies){
            try{
                Future<Body> res = executor
                        .submit(new ComputePositionTask(new Body(b), simulationModel.getBounds(), simulationModel.getDt()));
                results.add(res);
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }

        for(Future<Body> b : results){
            try{
                bodiesRes.add(b.get());
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return bodiesRes;
    }

}
