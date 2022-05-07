package exercise2;

import javax.swing.*;

public class View extends JFrame {

    private JFileChooser chooser;
    private String choosertitle;

    public View(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        JPanel panelChooser = new JPanel();
        this.getContentPane().add(panelChooser);
        this.chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        panelChooser.add(chooser);
        this.setVisible(true);
    }
}
