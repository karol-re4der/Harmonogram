package com.theglorious.re4der.harmonogram.Displaying;

import com.theglorious.re4der.harmonogram.Switch;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Calendar;

public class SelectedDay {
    
    public int hourSize = 0;
    public int startHour = 0;
    public int endHour = 24;
    public int startLevel = 0;
    
    public SelectedDay(){
        
    }
    public void render(Loop parent, Graphics2D draw, int gap){
        parent.cleanPanel();
        
        
        draw.setColor(Color.black);
        draw.drawLine(gap, gap+3*gap, parent.getWidth()-2*gap, gap+3*gap);
        draw.drawLine(gap, gap, parent.getWidth()-2*gap, gap);
        /*
        //load blocks
        if(blocks==null){
            blocks = new LinkedList();
            SaverLoader sl = new SaverLoader();
            blocks = sl.loadBlocks(blocks);
        }
        */
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

        /*
        //draw blocks
        for(Block block: blocks){
            if(block.dayOfWeek==Switch.chosenDay){
                //set level
                bubbleSort(blocks);
                block.level = 0;//startLevel;
                for(int i = 0; i<blocks.size(); i++){
                    if(blocks.get(i).dayOfWeek==Switch.chosenDay){
                        if(blocks.get(i)!=block){
                            if(blocks.get(i).level==block.level){
                                if(block.startsAt>=blocks.get(i).startsAt && block.startsAt<=blocks.get(i).startsAt+blocks.get(i).lengthMinutes || block.startsAt+block.lengthMinutes>=blocks.get(i).startsAt && block.startsAt+block.lengthMinutes<=blocks.get(i).startsAt+blocks.get(i).lengthMinutes){
                                    block.level++;
                                    i = 0;
                                }
                            }
                        }
                    }
                }
                
                
                //render block
                if(block.level+startLevel>=0){
                    draw.setColor(Color.black);
                    float mShift = (block.startsAt%60)/60f;
                    mShift*=hourSize;
                    int hShift = ((block.startsAt/60)-startHour)*hourSize;
                    int shift = (int)mShift+hShift;

                    int blockWidth = (int) ((block.lengthMinutes/60f)*hourSize);


                    int levelShift = (blockHeight+gap)*(block.level+startLevel);

                    draw.setColor(Color.gray);
                    if(block.active){
                        draw.setColor(Color.gray.brighter());
                    }
                    draw.fillRect(gap+shift, levelShift+gap*5, blockWidth, blockHeight);
                    draw.setColor(Color.black);
                    draw.drawRect(gap+shift, levelShift+gap*5, blockWidth, blockHeight);
                    draw.drawRect(gap+shift+1, levelShift+gap*5+1, blockWidth-2, blockHeight-2);

                    //draw text on blocks
                    String editionPointer = "|";
                    //-title
                    String text = block.title;
                    draw.setColor(Color.black);
                    if((activeEntry==1 && Switch.editMode.active) && block.active){
                        draw.setColor(Color.gray.darker());
                        text+=editionPointer;
                    }
                    Font font = new Font("Arial", Font.BOLD, 14);
                    FontMetrics metrics = draw.getFontMetrics(font);
                    draw.setFont(font);
                    draw.setClip(gap+shift, levelShift+gap*5, blockWidth-gap, blockHeight);
                    draw.drawString(text, 2*gap+shift, levelShift+gap*7);
                    //-teacher
                    text = block.teacher;
                    draw.setColor(Color.black);
                    if((activeEntry==2 && Switch.editMode.active) && block.active){
                        draw.setColor(Color.gray.darker());
                        text+=editionPointer;
                    }
                    font = new Font("Arial", Font.PLAIN, 12);
                    draw.setFont(font);
                    metrics = draw.getFontMetrics(font);
                    draw.drawString(text, 2*gap+shift, levelShift+gap*7+metrics.getHeight()+1);
                    //type
                    text = block.type;
                    draw.setColor(Color.black);
                    if((activeEntry==3 && Switch.editMode.active) && block.active){
                        draw.setColor(Color.gray.darker());
                        text+=editionPointer;
                    }
                    draw.drawString(text, 2*gap+shift, levelShift+gap*7+2*metrics.getHeight()+1);
                    //place and room
                    text = block.place;
                    draw.setColor(Color.black);
                    if((activeEntry==4 && Switch.editMode.active) && block.active){
                        draw.setColor(Color.gray.darker());
                        text+=editionPointer;
                    }
                    draw.drawString(text, 2*gap+shift, levelShift+gap*7+blockHeight-3*gap);
                    text = block.room;
                    font = new Font("Arial", Font.BOLD, 12);
                    draw.setFont(font);
                    draw.setColor(Color.black);
                    if((activeEntry==5 && Switch.editMode.active) && block.active){
                        draw.setColor(Color.gray.darker());
                        text+=editionPointer;
                    }
                    draw.drawString(" "+text, 2*gap+shift+metrics.stringWidth(block.place), levelShift+gap*7+blockHeight-3*gap);

                    draw.setClip(0, 0, this.getWidth(), this.getHeight());
                    //EXTEND HERE<<<------------------//
                }
            }
        }
        
        
        
        */
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
