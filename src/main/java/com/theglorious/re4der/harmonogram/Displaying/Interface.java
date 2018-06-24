package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Switch;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Interface {
    
    private static final Switch settings = Switch.getInstance();
    private Loop parent;
    
    public Interface(Loop parent){
        this.parent = parent;
    }
        
    private JLabel newButton(String text, int buttonWidth, int buttonHeight){
        JLabel button = new JLabel();
        button.setText(text);
        button.setSize(buttonWidth, buttonHeight);
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setOpaque(true);
        button.setBorder(new LineBorder(Color.black));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setBackground(Color.GRAY);
        button.addMouseListener(new MouseAdapter(){
            private Color cachedColor = Color.GRAY;
            private final Color hoverColor = new Color(Color.GRAY.brighter().getRed()+10, Color.GRAY.brighter().getGreen()+10, Color.GRAY.brighter().getBlue()+10);
            @Override
            public void mouseEntered(MouseEvent e){
                if(!e.getComponent().getBackground().equals(hoverColor)){
                    cachedColor = e.getComponent().getBackground();
                }
                e.getComponent().setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e){
                e.getComponent().setBackground(cachedColor);
            }
        });
        return button;
    }
    
    public void render(){
        int buttonWidth = 100;
        int buttonHeight = 30;
        //create panel
        JPanel interfacePanel = new JPanel();
        interfacePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        interfacePanel.setSize(parent.getWidth(), buttonHeight+2*settings.gap);
        interfacePanel.setLocation(0, parent.getHeight()-(buttonHeight+2*settings.gap));
        interfacePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        interfacePanel.setBackground(Color.WHITE);
        interfacePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, settings.gap, settings.gap));
        
        //back button
        JLabel button = newButton("Back", buttonWidth, buttonHeight);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                settings.switcher = 0;
                settings.editMode = false;
                settings.addingMode = false;
                settings.erasingMode = false;
                parent.synchronize();
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        
        //grid button
        button = newButton("Grid", buttonWidth, buttonHeight);
        if(settings.gridMode){
            button.setBackground(Color.GRAY.brighter());
        }
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                settings.gridMode = !settings.gridMode;
                parent.repaint();
            }
        });
        interfacePanel.add(button);
        
        //space button
        button = newButton("", buttonWidth, buttonHeight);
        button.setOpaque(false);
        button.setBorder(null);
        interfacePanel.add(button);
        
        //edit button 
        button = newButton("Edit", buttonWidth, buttonHeight);
        if(settings.editMode){
            button.setBackground(Color.GRAY.brighter());
        }
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                settings.editMode = !settings.editMode;
                if(!settings.editMode){
                    settings.addingMode = false;
                    settings.erasingMode = false;
                }
                parent.repaint();
                parent.cleanPanel();
            }
        });
        interfacePanel.add(button);
        if(settings.editMode){
            //save button
            button = newButton("Save", buttonWidth, buttonHeight);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    parent.save();
                    settings.editMode = false;
                    parent.repaint();
                    parent.cleanPanel();
                }
            });
            interfacePanel.add(button);

            //remove button
            button = newButton("Remove", buttonWidth, buttonHeight);
            if(settings.erasingMode){
                button.setBackground(Color.GRAY.brighter());
            }
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    settings.erasingMode = !settings.erasingMode;
                    settings.addingMode = false;
                    parent.repaint();
                    parent.cleanPanel();
                }
            });
            interfacePanel.add(button);

            //add button
            button = newButton("Add", buttonWidth, buttonHeight);
            if(settings.addingMode){
                button.setBackground(Color.GRAY.brighter());
            }
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    settings.addingMode = !settings.addingMode;
                    settings.erasingMode = false;
                    parent.repaint();
                    parent.cleanPanel();
                }
            });
            interfacePanel.add(button);
        }
        
        parent.add(interfacePanel);
    }
}
