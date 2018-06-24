package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Switch;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DaySelection {
    
    private static final Switch settings = Switch.getInstance();
    private Loop parent;
    
    public DaySelection(Loop parent){
        this.parent = parent;
    }
    public void render(Graphics2D draw){
        parent.cleanPanel();
        int dayFrameWidth = (int)(parent.getWidth()-(6*settings.gap))/5;

        if(true){
            //determine font size
            Font font = new Font("Arial" , Font.PLAIN, 1);
            while(true){
                FontMetrics testMetrics = draw.getFontMetrics(font);
                if(testMetrics.stringWidth("Poniedziałek")>dayFrameWidth-2*settings.gap){
                    break;
                }
                else{
                    font = new Font(font.getName(), font.getStyle(), font.getSize()+1);
                }
            }
            
            //calendar stuff
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_WEEK)-2;
            if(day==-1){
                day = 6;
            }
            for(int countIn = 0; countIn<cal.get(Calendar.DAY_OF_WEEK); countIn++){
                cal.roll(Calendar.DATE, false);
            }
            //draw buttons
            JPanel buttonsContainer = new JPanel();
            buttonsContainer.setBackground(Color.WHITE);
            buttonsContainer.setSize(parent.getParent().getWidth(), parent.getParent().getHeight()-settings.gap*2);
            buttonsContainer.setLocation(0, settings.gap);
            buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.LINE_AXIS));
            for(int count = 0; count<5; count++){
                //create button
                JPanel button = new JPanel();
                button.setLayout(new BoxLayout(button, BoxLayout.PAGE_AXIS));
                button.setBackground(Color.WHITE);
                button.setVisible(true);
                if(count!=day){
                    button.setBorder(BorderFactory.createLineBorder(Color.black));
                }
                else{
                    button.setBorder(BorderFactory.createLineBorder(Color.black, 2));
                }
                //set labels
                String timeName = "";
                switch(count){
                    case 0:
                        timeName = "Poniedziałek";
                        break;
                    case 1:
                        timeName = "Wtorek";
                        break;
                    case 2:
                        timeName = "Środa";
                        break;
                    case 3:
                        timeName = "Czwartek";
                        break;
                    case 4:
                        timeName = "Piątek";
                        break;
                }
                JLabel label = new JLabel(timeName);
                label.setFont(font);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                timeName = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
                JLabel subLabel = new JLabel(timeName);
                subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setMaximumSize(new Dimension(dayFrameWidth, parent.getHeight()));
                button.add(label);
                button.add(subLabel);
                //set on-click actions
                final int finalCount = count;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        settings.chosenDay = finalCount;
                        settings.switcher = 1;
                        parent.reloadBlocks();
                        parent.repaint();
                    }
                });
                //add to container
                buttonsContainer.add(Box.createHorizontalStrut(settings.gap));
                buttonsContainer.add(button);
            }
            buttonsContainer.add(Box.createHorizontalStrut(settings.gap));
            parent.add(buttonsContainer);
        }
    }
}
