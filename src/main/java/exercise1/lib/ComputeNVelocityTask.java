package exercise1.lib;

import exercise1.lib.Body;
import exercise1.lib.V2d;

import java.util.List;
import java.util.concurrent.Callable;

public class ComputeNVelocityTask implements Callable<Void> {

    private List<Body> bodies;
    private double dt;
    private int start;
    private int nBodiesForWorker;

    public ComputeNVelocityTask(List<Body> bodies, double dt, int start, int nBodiesForWorker){
        this.bodies = bodies;
        this.dt = dt;
        this.start = start;
        this.nBodiesForWorker = nBodiesForWorker;
    }

    public Void call() throws Exception {
        for(int i=start; i<start+nBodiesForWorker; i++){
            if (i < bodies.size()){
                Body b = bodies.get(i);
                /* compute total force on bodies */
                V2d totalForce = computeTotalForceOnBody(b);
                /* compute instant acceleration */
                V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());
                /* update velocity */
                b.updateVelocity(acc, dt);
            }
        }
        return null;
    }

    private V2d computeTotalForceOnBody(Body b) {
        V2d totalForce = new V2d(0, 0);
        /* compute total repulsive force */
        for (int j = 0; j < bodies.size(); j++) {
            Body otherBody = bodies.get(j);
            if (!b.equals(otherBody)) {
                try {
                    V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
                    totalForce.sum(forceByOtherBody);
                } catch (Exception ex) { }
            }
        }
        /* add friction force */
        totalForce.sum(b.getCurrentFrictionForce());
        return totalForce;
    }

}
