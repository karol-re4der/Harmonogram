package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Items.Block;
import com.theglorious.re4der.harmonogram.Items.BlockPanel;
import com.theglorious.re4der.harmonogram.Switch;
import com.theglorious.re4der.harmonogram.Utilties.SaverLoader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Loop extends JLayeredPane implements KeyListener, MouseListener{
    
    public int dayFrameWidth;
    public int activeEntry = 0;
    public int maxEntries = 5;
    public int roundingValue = 15;
    public LinkedList<Block> blocks;
    
    private Graphics2D draw;
    
    private final Interface userInterface = new Interface(this);
    private final DaySelection daySelection = new DaySelection(this);
    private final SelectedDay selectedDay = new SelectedDay(this);
    
    private static final Switch settings = Switch.getInstance();

    @Override
    public void paint(Graphics g){
        draw = (Graphics2D)g;
        if(this.getKeyListeners().length==0){
            addKeyListener(this);
        }
        if(this.getMouseListeners().length==0){
            addMouseListener(this);
            this.setFocusable(true);
        }
        
        
        
        //draw background
        draw.setColor(Color.white);
        draw.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        if(settings.switcher==0){
            daySelection.render( draw);
        }
        else if(settings.switcher==1){
            selectedDay.render(draw);
            userInterface.render();
        }
    }
    
    public void save(){
        SaverLoader sload = new SaverLoader();
        synchronize();
        sload.saveBlocks(blocks);
    }
        
    public void synchronize(){
        for(int i = 0; i<blocks.size(); i++){
            for(Component block: getComponents()){
                if(block instanceof BlockPanel){
                    BlockPanel castedComponent = (BlockPanel)block;
                    if(blocks.get(i).id==castedComponent.id){
                        //do stuff
                        blocks.get(i).title = ((JTextField) castedComponent.getComponent(0)).getText();
                        blocks.get(i).teacher = ((JTextField) castedComponent.getComponent(1)).getText();
                        blocks.get(i).type = ((JTextField) castedComponent.getComponent(2)).getText();
                        JPanel placePanel = (JPanel) castedComponent.getComponent(4);
                        blocks.get(i).place = ((JTextField) placePanel.getComponent(0)).getText();
                        blocks.get(i).room = ((JTextField) placePanel.getComponent(1)).getText();
                    }
                }
            }
        }
    }
    
    public void reloadBlocks(){
        blocks = new LinkedList();
        SaverLoader sl = new SaverLoader();
        blocks = sl.loadBlocks(blocks);
    }
    
    public void bubbleSort(){
        //move to array
        Block[] array = new Block[blocks.size()];
        for(int i = 0; i<blocks.size(); i++){
            array[i] = blocks.get(i);
        }
        
        //sort an array
        for(int i = 0; i<array.length; i++){
            if(i-1>=0 && i-1<=array.length){
                if(array[i-1].startsAt>array[i].startsAt){
                    Block cache = array[i-1];
                    array[i-1] = array[i];
                    array[i] = cache;
                    i = 0;
                }
            }
        }
        
        //move back to list
        blocks = new LinkedList();
        blocks.addAll(Arrays.asList(array));
    }
    
    public void levelizeBlocks(LinkedList<Block> list){
        for(int bi = 0; bi<blocks.size(); bi++){
            if(blocks.get(bi).dayOfWeek==settings.chosenDay){
                blocks.get(bi).level = 0;        //startLevel;
                for(int i = 0; i<blocks.size(); i++){
                    if(blocks.get(i).dayOfWeek==settings.chosenDay){
                        if(blocks.get(i)!=blocks.get(bi)){
                            if(blocks.get(i).level==blocks.get(bi).level){
                                if(blocks.get(bi).startsAt>=blocks.get(i).startsAt && blocks.get(bi).startsAt<=blocks.get(i).startsAt+blocks.get(i).lengthMinutes || blocks.get(bi).startsAt+blocks.get(bi).lengthMinutes>=blocks.get(i).startsAt && blocks.get(bi).startsAt+blocks.get(bi).lengthMinutes<=blocks.get(i).startsAt+blocks.get(i).lengthMinutes){
                                    blocks.get(bi).level++;
                                    i = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void moveTimeFrame(boolean advancing){
        if(advancing){
            if(selectedDay.startHour+1<24 && selectedDay.endHour+1<=24){
                selectedDay.startHour++;
                selectedDay.endHour++;
            }
        }
        else{
            if(selectedDay.startHour-1>=0 && selectedDay.endHour-1>0){
                selectedDay.startHour--;
                selectedDay.endHour--;
            }
        }
    }
    
    public void expandTimeFrame(boolean expanding){
        if(expanding){
            selectedDay.endHour++;
            selectedDay.startHour--;
            if(selectedDay.endHour>24){
                selectedDay.endHour = 24;
            }
            if(selectedDay.startHour<0){
                selectedDay.startHour = 0;
            }
        }
        else{
            if(selectedDay.endHour-selectedDay.startHour-2>1){
                selectedDay.endHour--;
                selectedDay.startHour++;
            }
        }
    }
    
    public void fitBlocks(){
        selectedDay.endHour = 0;
        selectedDay.startHour = 24;
        for(Block block: blocks){
            if(block.dayOfWeek==settings.chosenDay){
                if(block.startsAt/60<=selectedDay.startHour){
                    selectedDay.startHour = block.startsAt/60-1;
                }
                int length = block.lengthMinutes/60;
                if(block.startsAt/60+length>=selectedDay.endHour){
                    selectedDay.endHour = block.startsAt/60+length+1;
                }
            }
        }
    }

    public void addBlock(int startsAt){
        int modulo = Math.round((startsAt%roundingValue)/15f);
        int roundedTime = (startsAt/roundingValue)*roundingValue+modulo*roundingValue; 
        
        Block block = new Block();
        block.startsAt = roundedTime;
        
        block.title = "Title";
        block.type = "Type";
        block.teacher = "Teacher";
        block.room = "Room";
        block.place = "Place";
        block.dayOfWeek = settings.chosenDay;
        block.lengthMinutes = 60;
        block.setID(blocks);
        
        blocks.add(block);
        bubbleSort();
        levelizeBlocks(blocks);
        this.repaint();
    }

    public void removeBlock(int id){
        for(int i = 0; i<blocks.size(); i++){
            if(blocks.get(i).id==id){
                blocks.remove(i);
            }
        }
    }
    
    public void cleanPanel(){
        this.removeAll();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='-'){
            expandTimeFrame(true);
        }
        else if(e.getKeyChar()=='+'){
            expandTimeFrame(false);
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            moveTimeFrame(false);
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            moveTimeFrame(true);
        }
        else if(e.getKeyChar()=='='){
            fitBlocks();
        }
        else if(e.getKeyCode()==KeyEvent.VK_UP){
            selectedDay.startLevel--;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            selectedDay.startLevel++;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(settings.addingMode){
            //compute timeAt
            double minuteSize = selectedDay.hourSize/60f;
            int positionAt = e.getX()-settings.gap;
            int timeAt = (int) (positionAt/minuteSize)+selectedDay.startHour*60;
            //compute levelAt
            positionAt = e.getY()-settings.gap*5;
            int levelAt = positionAt/(selectedDay.blockHeight+settings.gap);
            int tak = positionAt-levelAt*(selectedDay.blockHeight+settings.gap);
            if(tak>selectedDay.blockHeight || tak<0){
                levelAt = -1;
            }
            addBlock(timeAt);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}