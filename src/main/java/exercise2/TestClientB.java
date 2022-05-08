package exercise2;

import io.vertx.core.Vertx;

public class TestClientB {
    public static void main(String[] args){
        Vertx v = Vertx.vertx();
        AsyncJavaParser lib = new AsyncJavaParser(v);
        Model model = new Model();
        Controller controller = new Controller(lib);
        v.deployVerticle(controller);
        new View(controller, model);
    }
}
