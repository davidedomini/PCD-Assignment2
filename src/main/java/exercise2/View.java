package exercise2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {

    JLabel labelDirectoryChoosed = new JLabel("No selected directory");
    Controller controller;

    public View(Controller controller){
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setLayout(new BorderLayout());

        // SELECTION OF THE DIRECTORY
        JPanel selectionPanel = new JPanel();
        this.getContentPane().add(BorderLayout.NORTH, selectionPanel);
        selectionPanel.add(labelDirectoryChoosed);
        JButton chooserButton = new JButton("Select a Directory");
        selectionPanel.add(chooserButton);
        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(chooser.showOpenDialog(selectionPanel) == JFileChooser.APPROVE_OPTION){
                    labelDirectoryChoosed.setText(chooser.getCurrentDirectory().toString());
                    System.out.println(chooser.getCurrentDirectory());
                } else {
                    labelDirectoryChoosed.setText("No selected directory");
                    System.out.println("No selected directory");
                }
            }
        });

        // VISUALIZING ANALYSIS OF PROJECT
        JPanel visualizingPanel = new JPanel();
        this.getContentPane().add(BorderLayout.CENTER, visualizingPanel);

        // CONTROL OF THE ANALYSIS
        JPanel controlPanel = new JPanel();
        this.getContentPane().add(BorderLayout.SOUTH, controlPanel);
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startAnalysis(labelDirectoryChoosed.getText());
            }
        });

        this.setVisible(true);
    }
}
