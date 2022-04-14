package exercise2.sync;

public class SimpleClient {
    public static void main(String[] args){
        SyncAnalyzer analyzer = new SyncAnalyzer();
        System.out.println(analyzer.getInterfaceReport("src/main/java/exercise2/sync/InterfacciaDiProva.java"));
    }
}
