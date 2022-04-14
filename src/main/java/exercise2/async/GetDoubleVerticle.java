package exercise2.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.Random;

public class GetDoubleVerticle extends AbstractVerticle {

    public Future<Double> getDouble(){
        System.out.println("Creo la promise di get double");
        Promise<Double> promise = Promise.promise();

        System.out.println("Setto il timeout di get double");
        this.getVertx().setTimer(1000, res -> {
            Random rand = new Random();
            Double value = rand.nextDouble();
            if (value > 0.5) {
                promise.complete(value);
            } else {
                promise.fail("Value below 0.5 " + value);
            }
        });
        System.out.println("Ritorno la future di get double");
        return promise.future();
    }

    public Future<Integer> getInt(){
        System.out.println("Creo la promise di get int");
        Promise<Integer> promise = Promise.promise();

        System.out.println("Setto il timeout di get int");
        this.getVertx().setTimer(1000, res -> {
            Random rand = new Random();
            Integer value = rand.nextInt();
            if (value > 0.5) {
                promise.complete(value);
            } else {
                promise.fail("Value below 0.5 " + value);
            }
        });
        System.out.println("Ritorno la future di get int");
        return promise.future();
    }


    public void start(){
        System.out.println("Verticle parto!");
    }
}
