package exercise3;

public class TestClientA {

    public static void main(String[] args){
        ReactiveJavaParser lib = new ReactiveJavaParser();
        lib
            .getInterfaceReport("src/main/java/common/examples/InterfacciaDiProva.java")
            .subscribe(System.out::println);

        lib
            .getClassReport("src/main/java/common/examples/ClasseDiProva.java")
            .subscribe(System.out::println);

        lib
            .getPackageReport("src/main/java/common/examples")
            .subscribe(System.out::println);

        lib
                .getProjectReport("src/main/java/common/examples")
                .subscribe(System.out::println);
    }
}
