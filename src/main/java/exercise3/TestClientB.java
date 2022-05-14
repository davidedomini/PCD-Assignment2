package exercise3;

import common.View;

import javax.swing.*;

public class TestClientB {

    public static void main(String[] args){
        ReactiveJavaParser lib = new ReactiveJavaParser();
        Controller controller = new Controller(lib);
        View view = new View(controller);
        controller.setView(view);
        /*SwingUtilities.invokeLater(()->{
            View view = new View(controller);
            controller.setView(view);
        });*/
    }

}
