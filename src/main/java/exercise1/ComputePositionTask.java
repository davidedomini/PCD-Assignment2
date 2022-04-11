package exercise1;

import exercise1.lib.Body;
import exercise1.lib.Boundary;

import java.util.concurrent.Callable;

public class ComputePositionTask implements Callable<Body> {
    private Body body;
    private Boundary bounds;
    private double dt;

    public ComputePositionTask(Body body, Boundary bounds, double dt){
        this.body = body;
        this.bounds = bounds;
        this.dt = dt;
    }

    @Override
    public Body call() throws Exception {
        body.updatePos(dt);
        /* check collisions with boundaries */
        body.checkAndSolveBoundaryCollision(bounds);
        return body;
    }
}
