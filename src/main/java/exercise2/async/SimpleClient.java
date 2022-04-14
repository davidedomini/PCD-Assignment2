package exercise2.async;

public class SimpleClient {

    public static void main(String[] args){
        AsyncJavaParser lib = new AsyncJavaParser();
        lib.getDouble()
                .onSuccess((Double res) -> {
                    System.out.println("Returned int: " + res);
                })
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });


        lib.getInteger()
                .onSuccess((Integer res) -> {
                    System.out.println("Returned int: " + res);
                })
                .onFailure((Throwable th)-> {
                    System.out.println("Returned error: " + th.getMessage());
                });
    }
}
