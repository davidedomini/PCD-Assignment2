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
    private int bodiesPerTask;
    private int poolSize;

    public MasterService(SimulationModel simulationModel, int poolSize, StopFlag stopFlag){
        this.simulationModel = simulationModel;
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.stopFlag = stopFlag;
        this.bodiesPerTask = (int) Math.ceil( (double) simulationModel.getnBodies() / poolSize);
        this.poolSize = poolSize;
    }

    public void run(){
        simulationModel.init();
        try{
            long start = Calendar.getInstance().getTimeInMillis();
            while(!simulationModel.isCompleted() && !stopFlag.getStopFlag()){
                computeNVelocities();
                computeNPositions();
                simulationModel.updateVirtualTime();
                simulationModel.update();
            }
            long finish = Calendar.getInstance().getTimeInMillis();
            System.out.println("Time: " + (finish-start) + " ms");
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    private void computeNVelocities(){
        List<Future<Void>> results = new ArrayList<>();
        for(int i = 0; i <= this.poolSize; i++){
            int start = i * this.bodiesPerTask;
            System.out.println("Assegno il compito vel al task " + i);
            try{
                Future<Void> res = executor
                        .submit(new ComputeNVelocityTask(simulationModel.getBodies(), simulationModel.getDt(), start, bodiesPerTask));
                results.add(res);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        for(Future<Void> r : results){
            try{
                r.get();
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }

    private void computeNPositions(){
        List<Future<Void>> results = new ArrayList<>();

        for(int i = 0; i <= this.poolSize; i++){
            int start = i * this.bodiesPerTask;
            try{
                Future<Void> res = executor
                        .submit(new ComputeNPositionsTask(simulationModel.getBodies(), simulationModel.getDt(), simulationModel.getBounds(), start, bodiesPerTask));
                results.add(res);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        for(Future<Void> r : results){
            try{
                r.get();
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }

}
