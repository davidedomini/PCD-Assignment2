package exercise1.lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MasterService extends Thread{

    private SimulationModel simulationModel;
    private ExecutorService executor;
    private StopFlag stopFlag;

    public MasterService(SimulationModel simulationModel, int poolSize, StopFlag stopFlag){
        this.simulationModel = simulationModel;
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.stopFlag = stopFlag;
    }

    public void run(){
        simulationModel.init();
        try{
            long start = Calendar.getInstance().getTimeInMillis();
            while(!simulationModel.isCompleted() && !stopFlag.getStopFlag()){
                List<Body> velRes = computeVelocity();
                List<Body> posRes = computePositions(velRes);
                simulationModel.updateVirtualTime();
                simulationModel.update(posRes);
                //System.out.println("ITER: " + simulationModel.getIter());
            }
            long finish = Calendar.getInstance().getTimeInMillis();
            System.out.println("Time: " + (finish-start) + " ms");
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
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
