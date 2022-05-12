package exercise2.lib;

public class Flag {
    private boolean flag;

    public Flag(){
        flag = false;
    }

    public synchronized boolean isSet(){
        return this.flag;
    }

    public synchronized void stop(){
        this.flag = true;
    }

    public synchronized void reset(){
        this.flag = false;
    }
}
