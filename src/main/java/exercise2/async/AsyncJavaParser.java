package exercise2.async;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class AsyncJavaParser {
    Vertx vertx;
    GetDoubleVerticle v;
    public AsyncJavaParser(){
        this.vertx = Vertx.vertx();
        this.v = new GetDoubleVerticle();
        vertx.deployVerticle(this.v);
    }

    public Future<Double> getDouble(){
        return v.getDouble();
    }

    public Future<Integer> getInteger(){
        return v.getInt();
    }
}
