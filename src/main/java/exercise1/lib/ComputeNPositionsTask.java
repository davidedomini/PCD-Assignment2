package exercise1.lib;

import java.util.List;
import java.util.concurrent.Callable;

public class ComputeNPositionsTask implements Callable<Void> {
    private Boundary bounds;
    private double dt;
    private List<Body> bodies;
    private int start;
    private int nBodiesForWorker;

    public ComputeNPositionsTask(List<Body> bodies, double dt, Boundary bounds, int start, int nBodiesForWorker){
        this.bodies = bodies;
        this.dt = dt;
        this.bounds = bounds;
        this.start = start;
        this.nBodiesForWorker = nBodiesForWorker;
    }

    @Override
    public Void call() throws Exception {
        for(int i=start; i<start+nBodiesForWorker; i++){
            if (i < bodies.size()){
                Body b = bodies.get(i);
                /* compute bodies new pos */
                b.updatePos(dt);
                /* check collisions with boundaries */
                b.checkAndSolveBoundaryCollision(bounds);
            }
        }
        return null;
    }
}
