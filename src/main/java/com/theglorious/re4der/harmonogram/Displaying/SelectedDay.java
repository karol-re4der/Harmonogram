package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Items.Block;
import com.theglorious.re4der.harmonogram.Items.BlockPanel;
import com.theglorious.re4der.harmonogram.Switch;
import com.theglorious.re4der.harmonogram.Utilties.SaverLoader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Calendar;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SelectedDay{
    public int hourSize = 0;
    public int startHour = 0;
    public int endHour = 24;
    public int startLevel = 0;
    public int blockHeight;
    private Loop parent;

    private int resizeGrap = 10;
    
    public SelectedDay(Loop parent){
        this.parent = parent;
    }

    private JTextField createField(String text){
        JTextField field = new JTextField();
        field.setText(text);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setBorder(null);
        field.setEditable(true);
        if(!Switch.editMode.active){
            field.setFocusable(false);
            field.setCursor(null);
            field.setEditable(false);
        }
        field.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                Component comp = e.getComponent();
                MouseEvent passedEvent = SwingUtilities.convertMouseEvent(comp, e, comp.getParent());
                comp.getParent().dispatchEvent(passedEvent);
            }
            @Override
            public void mouseExited(MouseEvent e){
                Component comp = e.getComponent();
                MouseEvent passedEvent = SwingUtilities.convertMouseEvent(comp, e, comp.getParent());
                comp.getParent().dispatchEvent(passedEvent);
            }
        });
        field.setForeground(Color.black);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        return field;
    }
    
    private BlockPanel createBlock(Block block, int gap){
        BlockPanel jBlock = new BlockPanel(block.id);
        
        //some weirdo mathematics
        float mShift = (block.startsAt%60)/60f;
        mShift*=hourSize;
        int hShift = ((block.startsAt/60)-startHour)*hourSize;
        int shift = (int)mShift+hShift;

        int blockWidth = (int) ((block.lengthMinutes/60f)*hourSize);


        int levelShift = (blockHeight+gap)*(block.level+startLevel);
        //
        
        //setting location and size
        int x = gap+shift;
        int y = levelShift+gap*5;
        jBlock.setLocation(x, y);
        jBlock.setSize(blockWidth, blockHeight);
        jBlock.setPreferredSize(new Dimension(blockWidth, blockHeight));
        jBlock.setMaximumSize(new Dimension(blockWidth, blockHeight));
        jBlock.setMinimumSize(new Dimension(blockWidth, blockHeight));
        
        //on-click action
        jBlock.addMouseMotionListener(new  MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if(Switch.editMode.active){
                    float clickTime = (e.getXOnScreen()-parent.getParent().getParent().getParent().getParent().getX()-2*gap);
                    clickTime/=hourSize;
                    int hour = (int)clickTime;
                    float minute = clickTime-hour;
                    minute*=60;
                    int roundedMinute = Math.round(minute/parent.roundingValue)*parent.roundingValue;
                    int newStartsAt = (int)(hour*60+roundedMinute);
                    block.startsAt=newStartsAt;
                }
            }
        });
        jBlock.addMouseListener(new MouseAdapter(){
            private Color cachedColor = Color.GRAY;
            private final Color hoverColor = new Color(Color.GRAY.brighter().getRed()+10, Color.GRAY.brighter().getGreen()+10, Color.GRAY.brighter().getBlue()+10);
            @Override
            public void mouseReleased(MouseEvent e){
                if(Switch.editMode.active){
                    parent.repaint();
                }
            }
            @Override
            public void mouseClicked(MouseEvent e){
                if(Switch.editMode.active){
                    if(Switch.erasingMode.active){
                        parent.removeBlock(((BlockPanel)e.getComponent()).id);
                        parent.repaint();
                    }
                    else if(e.getX()<resizeGrap){
                        if(block.lengthMinutes>=2*parent.roundingValue){
                            block.lengthMinutes-=parent.roundingValue;
                        }
                        parent.repaint();
                    }
                    else if(e.getX()>e.getComponent().getSize().getWidth()-resizeGrap){
                        block.lengthMinutes+=parent.roundingValue;
                        parent.repaint();
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e){
                if(!e.getComponent().getBackground().equals(hoverColor)){
                    cachedColor = e.getComponent().getBackground();
                }
                e.getComponent().setBackground(hoverColor);
                ((JPanel)e.getComponent()).getComponent(4).setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e){
                e.getComponent().setBackground(cachedColor);
                ((JPanel)e.getComponent()).getComponent(4).setBackground(cachedColor);
            }
        });
        
        //setting colors
        jBlock.setBackground(Color.GRAY);
        
        //setting borders
        jBlock.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        
        //set Layout
        jBlock.setLayout(new BoxLayout(jBlock, BoxLayout.PAGE_AXIS));
        
        //creating text fields
        //-Title
        JTextField title = createField(block.title);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setLocation(0, 0);
        jBlock.add(title);
        //-teacher
        JTextField teacher = createField(block.teacher);
        jBlock.add(teacher);
        //-type
        JTextField type = createField(block.type);
        jBlock.add(type);
        jBlock.add(Box.createVerticalStrut((int)(gap*2.3)));
        //-place and room
        JPanel container = new JPanel();
        container.addMouseListener(new MouseAdapter(){ //dispatching hovering mouse event
            @Override
            public void mouseEntered(MouseEvent e){
                Component comp = e.getComponent();
                MouseEvent passedEvent = SwingUtilities.convertMouseEvent(comp, e, comp.getParent());
                comp.getParent().dispatchEvent(passedEvent);
            }
            @Override
            public void mouseExited(MouseEvent e){
                Component comp = e.getComponent();
                MouseEvent passedEvent = SwingUtilities.convertMouseEvent(comp, e, comp.getParent());
                comp.getParent().dispatchEvent(passedEvent);
            }
        });
        container.setBackground(Color.GRAY);
        container.setBorder(null);
        container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
        JTextField place = createField(block.place);
        container.add(place);
        JTextField room = createField(block.room);
        room.setFont(new Font("Arial", Font.BOLD, 12));
        container.add(room);
        jBlock.add(container);
        return jBlock;
    }
    
    public void render(Loop parent, Graphics2D draw, int gap){
        parent.cleanPanel();
        
        
        draw.setColor(Color.black);
        draw.drawLine(gap, gap+3*gap, parent.getWidth()-2*gap, gap+3*gap);
        draw.drawLine(gap, gap, parent.getWidth()-2*gap, gap);
        
        hourSize = (parent.getWidth()-2*gap)/(endHour-startHour);
        
        //borders
        for(int count = 0; count<(endHour-startHour);count++){
            draw.drawLine(gap+hourSize*count, gap, gap+hourSize*count, parent.getHeight()-gap);
            if(Switch.grid.active){
                draw.drawLine(gap+hourSize*count+1, gap, gap+hourSize*count+1, parent.getHeight()-gap);
                draw.drawLine(gap+hourSize*count-1, gap, gap+hourSize*count-1, parent.getHeight()-gap);
            }
            
            //set font
            Font font = new Font("Arial", Font.PLAIN, 1);
            FontMetrics metrics = draw.getFontMetrics(font);
            while(true){
                if(metrics.getHeight()<=gap*2){
                    font = new Font(font.getName(), font.getStyle(), font.getSize()+1);
                    metrics = draw.getFontMetrics(font);
                }
                else{
                    break;
                }
            }
            draw.setFont(font);
            
            //draw hours
            draw.setClip(gap+hourSize*count, gap, hourSize-gap, 3*gap);
            draw.drawString(String.valueOf(startHour+count)+":00", gap+(gap/2)+count*hourSize, gap*3+(gap/2));
            draw.setClip(0, 0, parent.getWidth(), parent.getHeight());

        }
        
        //draw additional grid
        if(Switch.grid.active){
            int howMuch = endHour-startHour;
            draw.setColor(Color.black);
            for(int count = 0; count<howMuch; count++){
                for(int countIns = 0; countIns<4; countIns++){
                    draw.drawLine(gap+count*hourSize+countIns*(hourSize/4), 4*gap, gap+count*hourSize+countIns*(hourSize/4), parent.getHeight()-gap);
                }
            }
        }

        
        //draw blocks
        blockHeight = 10*gap;
        for(int bi = 0; bi<parent.blocks.size(); bi++){
            if(parent.blocks.get(bi).dayOfWeek==Switch.chosenDay){
                //set level
                parent.bubbleSort(parent.blocks);
                parent.blocks.get(bi).level = 0;//startLevel;
                for(int i = 0; i<parent.blocks.size(); i++){
                    if(parent.blocks.get(i).dayOfWeek==Switch.chosenDay){
                        if(parent.blocks.get(i)!=parent.blocks.get(bi)){
                            if(parent.blocks.get(i).level==parent.blocks.get(bi).level){
                                if(parent.blocks.get(bi).startsAt>=parent.blocks.get(i).startsAt && parent.blocks.get(bi).startsAt<=parent.blocks.get(i).startsAt+parent.blocks.get(i).lengthMinutes || parent.blocks.get(bi).startsAt+parent.blocks.get(bi).lengthMinutes>=parent.blocks.get(i).startsAt && parent.blocks.get(bi).startsAt+parent.blocks.get(bi).lengthMinutes<=parent.blocks.get(i).startsAt+parent.blocks.get(i).lengthMinutes){
                                    parent.blocks.get(bi).level++;
                                    i = 0;
                                }
                            }
                        }
                    }
                }
                
                
                //render block
                if(parent.blocks.get(bi).level+startLevel>=0){
                    parent.add(createBlock(parent.blocks.get(bi), gap));
                }
            }
        }

        //draw current hour line
        int currHour = 0;
        int currMinute = 0;
        int currTimeSize = 0;

        Calendar cal = Calendar.getInstance();
        currHour = (cal.get(Calendar.HOUR_OF_DAY)-startHour)*hourSize;
        float test = (float)cal.get(Calendar.MINUTE)/60;
        currMinute = (int)(test*hourSize);
        currTimeSize = currHour+currMinute;

        if(cal.get(Calendar.DAY_OF_WEEK)-2==Switch.chosenDay){
            draw.drawLine(gap+currTimeSize-1, gap*4, gap+currTimeSize-1, parent.getHeight()-gap);
            draw.drawLine(gap+currTimeSize, gap*4, gap+currTimeSize, parent.getHeight()-gap);
            draw.drawLine(gap+currTimeSize+1, gap*4, gap+currTimeSize+1, parent.getHeight()-gap);
        } 
    }
}
