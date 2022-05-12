package exercise3;

public class TestClientA {

    public static void main(String[] args){
        ReactiveJavaParser lib = new ReactiveJavaParser();
        lib
            .getInterfaceReport("src/main/java/exercise2/examples/InterfacciaDiProva.java")
            .subscribe(System.out::println);

        lib
            .getClassReport("src/main/java/exercise2/examples/ClasseDiProva.java")
            .subscribe(System.out::println);
    }

}
