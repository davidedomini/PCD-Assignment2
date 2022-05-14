package exercise2;

import io.vertx.core.Vertx;


public class TestClientA {
    public static void main(String[] args){
        AsyncJavaParser lib = new AsyncJavaParser(Vertx.vertx());

        lib.getInterfaceReport("src/main/java/common/examples/InterfacciaDiProva.java")
                .onSuccess(System.out::println)
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });

        lib.getClassReport("src/main/java/common/examples/ClasseDiProva.java")
                .onSuccess(System.out::println)
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });

        lib.getPackageReport("src/main/java/common/examples")
                .onSuccess(System.out::println)
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });

        lib.getProjectReport("src/main/java/common/examples")
                .onSuccess(System.out::println)
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });
    }
}
