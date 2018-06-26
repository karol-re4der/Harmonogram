package com.theglorious.re4der.harmonogram.Items;

import java.util.LinkedList;

public class Block {
    
    //obligatory
    public int dayOfWeek;
    public String place;
    public String room;
    public String type;
    public String teacher;
    public int startsAt;
    public int lengthMinutes;
    public String title;
    
    //display
    public int level = 0;
    public int id;
    
    public boolean equals(Block block){
        if(dayOfWeek!=block.dayOfWeek)
            return false;
        if(startsAt!=block.startsAt)
            return false;
        if(lengthMinutes!=block.lengthMinutes)
            return false;
        if(!title.equals(block.title))
            return false;
        if(!teacher.equals(block.teacher))
            return false;
        if(!type.equals(block.type))
            return false;
        if(!place.equals(block.place))
            return false;
        if(!room.equals(block.room))
            return false;
        return true;
    }
    
    public Block(){
        
    }

    public void setID(LinkedList<Block> blocks){
        int fooId = 0;
        //set id
        for(int ii = 0; ii<blocks.size(); ii++){
            if(blocks.get(ii).id==fooId){
                fooId++;
                ii = 0;
            }
        }
        id = fooId;
    }
}
