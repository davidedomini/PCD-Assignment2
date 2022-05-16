package common;

import exercise2.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {

    JLabel labelDirectoryChoosed = new JLabel("No selected directory");
    ControllerInterface controller;
    JTextArea textAreaReport = new JTextArea();;

    public View(ControllerInterface controller){
        this.controller = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());

        // SELECTION OF THE DIRECTORY
        JPanel selectionPanel = new JPanel();
        this.getContentPane().add(BorderLayout.NORTH, selectionPanel);
        selectionPanel.add(labelDirectoryChoosed);
        JButton chooserButton = new JButton("Select a Directory");
        selectionPanel.add(chooserButton);
        chooserButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(chooser.showOpenDialog(selectionPanel) == JFileChooser.APPROVE_OPTION){
                labelDirectoryChoosed.setText(chooser.getSelectedFile().toString());
                System.out.println(chooser.getSelectedFile());
            } else {
                labelDirectoryChoosed.setText("No selected directory");
                System.out.println("No selected directory");
            }
        });

        // VISUALIZING ANALYSIS OF PROJECT
        JPanel visualizingPanel = new JPanel();
        JScrollPane scroll = new JScrollPane (textAreaReport);
        textAreaReport.setEditable(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.getContentPane().add(BorderLayout.CENTER, scroll);

        // CONTROL OF THE ANALYSIS
        JPanel controlPanel = new JPanel();
        this.getContentPane().add(BorderLayout.SOUTH, controlPanel);
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            textAreaReport.setText("");
            controller.startAnalysis(labelDirectoryChoosed.getText());
        });
        stopButton.addActionListener(e -> {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            labelDirectoryChoosed.setText("No selected directory");
            controller.stopAnalysis();
        });

        this.setVisible(true);
    }

    public void notifyUpdates(String updates){
        display(updates);
    }

    private void display(String updates){
        try {
            SwingUtilities.invokeAndWait(() -> {
                textAreaReport.append(updates + "\n");
            });
        } catch (Exception ex) {}
    }

}
