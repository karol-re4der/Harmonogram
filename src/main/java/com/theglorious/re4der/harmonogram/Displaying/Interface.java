package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Switch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Interface {
    public Interface(){
        
    }
        
    private JButton newButton(String text, int buttonWidth, int buttonHeight){
        JButton button = new JButton();
        button.setText(text);
        button.setSize(buttonWidth, buttonHeight);
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setOpaque(true);
        return button;
    }
    
    public void render(Loop parent, int gap){
        int buttonWidth = 100;
        int buttonHeight = 30;
        //create panel
        JPanel interfacePanel = new JPanel();
        interfacePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        interfacePanel.setSize(parent.getWidth(), buttonHeight+2*gap);
        interfacePanel.setLocation(0, parent.getHeight()-(buttonHeight+2*gap));
        //interfacePanel.setBackground(new Color(0, 0, 0, 0));
        interfacePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, gap, gap));
        
        //back button
        JButton button = newButton("Back", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Switch.switcher = 0;
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        //save button
        button = newButton("Save", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parent.save();
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        //grid button
        button = newButton("Grid", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Switch.grid.active = !Switch.grid.active;
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        //add button
        button = newButton("Add", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Switch.addingMode.active = true;
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        //edit button
        button = newButton("Edit", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Switch.editMode.active = !Switch.editMode.active;
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        parent.add(interfacePanel);
    }
}
