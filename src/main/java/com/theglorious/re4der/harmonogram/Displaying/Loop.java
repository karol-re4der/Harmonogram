package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Items.Block;
import com.theglorious.re4der.harmonogram.Items.Button;
import com.theglorious.re4der.harmonogram.Switch;
import com.theglorious.re4der.harmonogram.Utilties.SaverLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.JLayeredPane;

public class Loop extends JLayeredPane implements KeyListener{
    
    public int gap = 10;
    public int dayFrameWidth;
    public int activeEntry = 0;
    public int maxEntries = 5;
    public int roundingValue = 15;
    public int blockHeight = 10*gap;
    public LinkedList<Block> blocks;
    public LinkedList<Button> buttons = new LinkedList();
    
    private Graphics2D draw;
    
    private final Interface userInterface = new Interface();
    private final DaySelection daySelection = new DaySelection();
    private final SelectedDay selectedDay = new SelectedDay();
    
    public void paint(Graphics g){
        draw = (Graphics2D)g;
        if(this.getMouseListeners().length==0){
            //addMouseListener(this);
            addKeyListener(this);
            this.setFocusable(true);
        }
        
        //draw background
        draw.setColor(Color.white);
        draw.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        if(Switch.switcher==0){
            daySelection.render(this, draw, gap);
        }
        else if(Switch.switcher==1){
            selectedDay.render(this, draw, gap);
            userInterface.render(this, gap);
        }
    }
    
    public void save(){
        SaverLoader sload = new SaverLoader();
        sload.saveBlocks(blocks);
    }
    
    public void bubbleSort(LinkedList<Block> list){
        //move to array
        Block[] array = new Block[list.size()];
        for(int i = 0; i<list.size(); i++){
            array[i] = list.get(i);
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
        list = new LinkedList();
        for(Block i: array){
            list.add(i);
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
            if(block.dayOfWeek==Switch.chosenDay){
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
    
    public void removeBlock(){
        for(int i = 0; i<blocks.size(); i++){
            if(blocks.get(i).active){
                blocks.remove(i);
                break;
            }
        }
    }
    
    public void addBlock(int startsAt){
        int modulo = Math.round((startsAt%roundingValue)/15f);
        int roundedTime = (startsAt/roundingValue)*roundingValue+modulo*roundingValue; 
        
        String placeholderText = "placeholder";
        
        Block block = new Block();
        block.startsAt = roundedTime;
        
        block.title = placeholderText;
        block.type = placeholderText;
        block.teacher = placeholderText;
        block.room = placeholderText;
        block.place = placeholderText;
        block.dayOfWeek = Switch.chosenDay;
        block.lengthMinutes = 60;
        
        
        block.active = true;

        blocks.add(block);
    }
    
    public void moveBlock(boolean advancing){
        for(Block i: blocks){
            if(i.active){
                if(advancing){
                    if(i.startsAt+roundingValue<24*60){
                        i.startsAt+=roundingValue;
                    }
                }
                else{
                    if(i.startsAt-roundingValue>=0){
                        i.startsAt-=roundingValue;
                    }
                }
            }
        }
    }
    
    public void resizeBlock(boolean extending){
        for(Block i: blocks){
            if(i.active){
                if(extending){
                    i.lengthMinutes+=roundingValue;
                }
                else{
                    if(i.lengthMinutes-roundingValue>=roundingValue){
                        i.lengthMinutes -= roundingValue;
                    }
                }
            }
        }
    }
    
    public void cleanPanel(){
        this.removeAll();
    }

    private void drawSelectedDay(Graphics2D draw){
        
    }

    /*
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(Switch.switcher==0){
            for(int count = 0; count<5; count++){
                if(e.getX()>=gap+(dayFrameWidth*count)+(gap*count) && e.getX()<=gap+(dayFrameWidth*count)+(gap*count)+dayFrameWidth){
                    if(e.getY()>=gap && e.getY()<=gap+this.getHeight()-(2*gap)){
                        Switch.chosenDay=count;
                        
                        Switch.switcher=1;
                    }
                }
            }
        }
        else if(Switch.switcher==1){
            //buttons
            if(buttons.get(0).isClicked(e.getX(), e.getY())){
                Switch.switcher = 0;
                activeEntry = 0;
                for(Block i: blocks){
                    i.active = false;
                }
            }
            else if(buttons.get(1).isClicked(e.getX(), e.getY())){
                save();
            }
            else if(buttons.get(2).isClicked(e.getX(), e.getY())){
                Switch.grid.active = !Switch.grid.active;
            }
            else if(buttons.get(3).isClicked(e.getX(), e.getY())){
                Switch.addingMode.active = !Switch.addingMode.active;
            }
            else if(buttons.get(4).isClicked(e.getX(), e.getY())){
                Switch.editMode.active = !Switch.editMode.active;
            }
            
            //compute click-time
            else{
                //compute timeAt
                double minuteSize = hourSize/60f;
                int positionAt = e.getX()-gap;
                int timeAt = (int) (positionAt/minuteSize)+startHour*60;
                //compute levelAt
                positionAt = e.getY()-gap*5;
                int levelAt = positionAt/(blockHeight+gap);
                int tak = positionAt-levelAt*(blockHeight+gap);
                if(tak>blockHeight || tak<0){
                    levelAt = -1;
                }

                //setting activity
                for(Block i: blocks){
                    if(i.dayOfWeek==Switch.chosenDay){
                        if((timeAt>=i.startsAt && timeAt<=i.startsAt+i.lengthMinutes-1) && levelAt==i.level){
                            if(Switch.editMode.active){
                                i.active = true;
                                activeEntry = 0;
                            }
                        }
                        else{
                            i.active = false;
                        }
                    }
                }
                
                //adding blocks
                if(Switch.addingMode.active){
                    addBlock(timeAt);
                }
            }
        }
        
        
        
        this.repaint();
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

    */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(Switch.editMode.active){
            if(e.getKeyChar()=='+'){
                resizeBlock(true);
            }
            else if(e.getKeyChar()=='-'){
                resizeBlock(false);
            }
            else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                moveBlock(false);
            }
            else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                moveBlock(true);
            }
            else if(e.getKeyCode()==KeyEvent.VK_DELETE){
                removeBlock();
            }
            else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                if(activeEntry+1<=maxEntries){
                    activeEntry++;
                }
                else{
                    activeEntry = 0;
                }
            }
            else if(e.getKeyCode()==KeyEvent.VK_UP){
                if(activeEntry-1>=0){
                    activeEntry--;
                }
                else{
                    activeEntry = maxEntries;
                }
            }
            else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
                backSpaceFromEntry();
            }
            else{
                addToEntry(e.getKeyChar());
            }
        }
        else{
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
            else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                selectedDay.startLevel--;
            }
            else if(e.getKeyCode()==KeyEvent.VK_UP){
                selectedDay.startLevel++;
            }
        }
        repaint();

    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    private void backSpaceFromEntry() {
        for(Block i: blocks){
            if(i.active){
                if(activeEntry>0 && activeEntry<=maxEntries){
                    if(activeEntry==1 && i.title.length()>0){
                        i.title = i.title.substring(0, i.title.length()-1);
                    }
                    else if(activeEntry==2 && i.teacher.length()>0){
                        i.teacher = i.teacher.substring(0, i.teacher.length()-1);
                    }
                    else if(activeEntry==3 && i.type.length()>0){
                        i.type = i.type.substring(0, i.type.length()-1);
                    }
                    else if(activeEntry==4 && i.place.length()>0){
                        i.place = i.place.substring(0, i.place.length()-1);
                    }
                    else if(activeEntry==5 && i.room.length()>0){
                        i.room = i.room.substring(0, i.room.length()-1);
                    }
                }
                break;
            }
        }
    }



    private void addToEntry(char keyChar) {
        if(keyChar>=32 && keyChar<=126){
            for(Block i: blocks){
                if(i.active){
                    if(activeEntry>0 && activeEntry<=maxEntries){
                        switch (activeEntry) {
                            case 1:
                                i.title = i.title.concat(String.valueOf(keyChar));
                                break;
                            case 2:
                                i.teacher = i.teacher.concat(String.valueOf(keyChar));
                                break;
                            case 3:
                                i.type = i.type.concat(String.valueOf(keyChar));
                                break;
                            case 4:
                                i.place = i.place.concat(String.valueOf(keyChar));
                                break;
                            case 5:
                                i.room = i.room.concat(String.valueOf(keyChar));
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                }
            }    
        }
    }
}
