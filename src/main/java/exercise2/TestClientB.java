package exercise2;

import io.vertx.core.Vertx;

public class TestClientB {
    public static void main(String[] args){
        Vertx v = Vertx.vertx();
        AsyncJavaParser lib = new AsyncJavaParser(v);
        Controller controller = new Controller(lib);
        v.deployVerticle(controller);
        View view = new View(controller);
        controller.setView(view);
    }
}
