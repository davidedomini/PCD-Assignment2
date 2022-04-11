package exercise1;

import exercise1.lib.Body;
import exercise1.lib.V2d;

import java.util.List;
import java.util.concurrent.Callable;

public class ComputeVelocityTask implements Callable<Body> {

    private Body body;
    private double dt;
    private List<Body> bodies;

    public ComputeVelocityTask(Body body, double dt, List<Body> bodies){
        this.body = body;
        this.dt = dt;
        this.bodies = bodies;
    }

    @Override
    public Body call() throws Exception {
        V2d totalForce = computeTotalForceOnBody(body);
        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / body.getMass());
        /* update velocity */
        body.updateVelocity(acc, dt);
        return body;
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
