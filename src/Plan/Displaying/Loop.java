package Plan.Displaying;

import Plan.Items.Block;
import Plan.Switch;
import Plan.Utilties.SaverLoader;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.LinkedList;
import javax.swing.JPanel;

public class Loop extends JPanel implements MouseListener{
    
    public int distance = 10;
    public int buttonSizeX;
    public LinkedList<Block> blocks;
    private int hourSize = 0;
    private int startHour = 24;
    private int endHour = 0;
    
    public void paint(Graphics g){
        Graphics2D draw = (Graphics2D)g;
        if(this.getMouseListeners().length==0){
            addMouseListener(this);
        }
        
        //draw background
        draw.setColor(Color.white);
        draw.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        if(Switch.switcher==0){
            drawDaySelection(draw);
        }
        else if(Switch.switcher==1){
            drawSelectedDay(draw);
        }
    }
    
    public void save(){
        SaverLoader sload = new SaverLoader();
        sload.saveBlocks(blocks);
    }
    
    public void removeBlock(){
        for(int i = 0; i<blocks.size(); i++){
            if(blocks.get(i).active){
                blocks.remove(i);
                break;
            }
        }
        save();
    }
    
    private void drawDaySelection(Graphics2D draw){
        buttonSizeX = (int)(this.getWidth()-(6*distance))/5;
        //test
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK)-2;
        if(day==-1){
            day = 6;
        }
        
        Font font = new Font("Arial" , Font.PLAIN, 1);
        while(true){
            FontMetrics testMetrics = draw.getFontMetrics(font);
            if(testMetrics.stringWidth("Poniedziałek")>buttonSizeX-2*distance){
                break;
            }
            else{
                font = new Font(font.getName(), font.getStyle(), font.getSize()+1);
            }
        }
        
        
        FontMetrics metrics = draw.getFontMetrics(font);
        draw.setFont(font);
        int fontHeight = metrics.getAscent();
        
            
        for(int countIn = 0; countIn<cal.get(Calendar.DAY_OF_WEEK); countIn++){
            cal.roll(Calendar.DATE, false);
        }
        //draw buttons
        draw.setColor(Color.black);
        for(int count = 0; count<5; count++){
            draw.setFont(font);
            metrics = draw.getFontMetrics(font);
            
            //borders
            draw.drawRect(distance+(buttonSizeX*count)+(distance*count), distance, buttonSizeX, this.getHeight()-(2*distance));
            if(count==day){
                draw.drawRect(distance+(buttonSizeX*count)+(distance*count)+1, distance+1, buttonSizeX-2, this.getHeight()-(2*distance)-2);
            }
            //names
            String timeName = null;
            draw.setClip(distance+(buttonSizeX*count)+(distance*count)+1, distance+1, buttonSizeX-2, this.getHeight()-(2*distance)-2);
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
            metrics.stringWidth(timeName);
            int advance = ((buttonSizeX-2*distance)-metrics.stringWidth(timeName))/2;
            draw.drawString(timeName, distance+distance+(buttonSizeX*count)+(distance*count)+advance, 2*distance+fontHeight);   
            
            //draw calendar time

            Font font2 = new Font(font.getName(), Font.PLAIN, font.getSize()/3);
            metrics = draw.getFontMetrics(font2);
            draw.setFont(font2);
            
            if(count>0){
                cal.roll(Calendar.DATE, true);
            }
            timeName = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
            advance = ((buttonSizeX-2*distance)-metrics.stringWidth(timeName))/2;
            draw.drawString(timeName, distance+distance+(buttonSizeX*count)+(distance*count)+advance, 2*distance+fontHeight*2);   
            
            
            
            
            draw.setClip(0, 0, this.getWidth(), this.getHeight());
        }
        cal = Calendar.getInstance();
    }

    private void drawSelectedDay(Graphics2D draw){
        
        
        draw.setColor(Color.black);
        draw.drawLine(distance, distance+3*distance, this.getWidth()-2*distance, distance+3*distance);
        draw.drawLine(distance, distance, this.getWidth()-2*distance, distance);
        
        //load blocks
        if(blocks==null){
            blocks = new LinkedList();
            SaverLoader sl = new SaverLoader();
            blocks = sl.loadBlocks(blocks);
        }
        
        //set
        for(Block block: blocks){
            if(block.dayOfWeek==Switch.chosenDay){
                if(block.startsAt/60<=startHour){
                    startHour = block.startsAt/60-1;
                }
                int length = block.lengthMinutes/60;
                if(block.startsAt/60+length>=endHour){
                    endHour = block.startsAt/60+length+1;
                }
            }
        }
        hourSize = (this.getWidth()-2*distance)/(endHour-startHour);
        
        //borders
        for(int count = 0; count<(endHour-startHour);count++){
            draw.drawLine(distance+hourSize*count, distance, distance+hourSize*count, this.getHeight()-distance);
            if(Switch.grid){
                draw.drawLine(distance+hourSize*count+1, distance, distance+hourSize*count+1, this.getHeight()-distance);
                draw.drawLine(distance+hourSize*count-1, distance, distance+hourSize*count-1, this.getHeight()-distance);
            }
            
            //set font
            Font font = new Font("Arial", Font.PLAIN, 1);
            FontMetrics metrics = draw.getFontMetrics(font);
            while(true){
                if(metrics.getHeight()<=distance*2){
                    font = new Font(font.getName(), font.getStyle(), font.getSize()+1);
                    metrics = draw.getFontMetrics(font);
                }
                else{
                    break;
                }
            }
            draw.setFont(font);
            
            //draw hours
            draw.setClip(distance+hourSize*count, distance, hourSize-distance, 3*distance);
            draw.drawString(String.valueOf(startHour+count)+":00", distance+(distance/2)+count*hourSize, distance*3+(distance/2));
            draw.setClip(0, 0, this.getWidth(), this.getHeight());

        }
        
        //draw additional grid
        if(Switch.grid){
            int howMuch = endHour-startHour;
            draw.setColor(Color.black);
            for(int count = 0; count<howMuch; count++){
                for(int countIns = 0; countIns<4; countIns++){
                    draw.drawLine(distance+count*hourSize+countIns*(hourSize/4), 4*distance, distance+count*hourSize+countIns*(hourSize/4), this.getHeight()-distance);
                }
            }
        }

        //draw blocks
        for(Block block: blocks){
            if(block.dayOfWeek==Switch.chosenDay){
                draw.setColor(Color.black);
                float mShift = (block.startsAt%60)/60f;
                mShift*=hourSize;
                int hShift = ((block.startsAt/60)-startHour)*hourSize;
                int shift = (int)mShift+hShift;
                
                int blockSize = (int) ((block.lengthMinutes/60f)*hourSize);
                
                draw.setColor(Color.gray);
                if(block.active){
                    draw.setColor(Color.gray.brighter());
                }
                draw.fillRect(distance+shift, distance*5, blockSize, 10*distance);
                draw.setColor(Color.black);
                draw.drawRect(distance+shift, distance*5, blockSize, 10*distance);
                draw.drawRect(distance+shift+1, distance*5+1, blockSize-2, 10*distance-2);
                
                //draw text on blocks
                //-title
                Font font = new Font("Arial", Font.BOLD, 14);
                FontMetrics metrics = draw.getFontMetrics(font);
                draw.setFont(font);
                draw.setClip(distance+shift, distance*5, blockSize-distance, 10*distance);
                draw.drawString(block.title, 2*distance+shift, distance*7);
                //-teacher
                font = new Font("Arial", Font.PLAIN, 12);
                draw.setFont(font);
                metrics = draw.getFontMetrics(font);
                draw.drawString(block.teacher, 2*distance+shift, distance*7+metrics.getHeight()+1);
                //type
                draw.drawString(block.type, 2*distance+shift, distance*7+2*metrics.getHeight()+1);
                //place
                draw.drawString(block.place, 2*distance+shift, distance*7+10*distance-3*distance);
                font = new Font("Arial", Font.BOLD, 12);
                draw.setFont(font);
                draw.drawString(" "+block.room, 2*distance+shift+metrics.stringWidth(block.place), distance*7+10*distance-3*distance);
                
                draw.setClip(0, 0, this.getWidth(), this.getHeight());
                //EXTEND HERE<<<------------------//
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
            draw.drawLine(distance+currTimeSize-1, distance*4, distance+currTimeSize-1, this.getHeight()-distance);
            draw.drawLine(distance+currTimeSize, distance*4, distance+currTimeSize, this.getHeight()-distance);
            draw.drawLine(distance+currTimeSize+1, distance*4, distance+currTimeSize+1, this.getHeight()-distance);
        }

        //draw return button
        Font font = new Font("Arial", Font.BOLD, 15);
        draw.setFont(font);
        draw.setColor(Color.gray);
        draw.fillRect(this.getWidth()-distance-60, this.getHeight()-4*distance, 60, 3*distance);
        draw.setColor(Color.black);
        draw.drawRect(this.getWidth()-distance-60, this.getHeight()-4*distance, 60, 3*distance);
        draw.drawRect(this.getWidth()-distance-60+1, this.getHeight()-4*distance+1, 60-2, 3*distance-2);
        draw.drawString("Back", this.getWidth()-60, this.getHeight()-2*distance);

        //draw gridButton
        draw.setColor(Color.gray);
        if(Switch.grid==true){
            draw.setColor(Color.gray.brighter());
        }
        draw.fillRect(this.getWidth()-distance-120-distance, this.getHeight()-4*distance, 60, 3*distance);
        draw.setColor(Color.black);
        draw.drawRect(this.getWidth()-distance-120-distance, this.getHeight()-4*distance, 60, 3*distance);
        draw.drawRect(this.getWidth()-distance-120+1-distance, this.getHeight()-4*distance+1, 60-2, 3*distance-2);
        draw.drawString("Grid", this.getWidth()-120-distance, this.getHeight()-2*distance);
        
        //draw add button
        draw.setColor(Color.gray);
        if(Switch.addingMode==true){
            draw.setColor(Color.gray.brighter());
        }
        draw.fillRect(this.getWidth()-180-distance*3, this.getHeight()-4*distance, 60, 3*distance);
        draw.setColor(Color.black);
        draw.drawRect(this.getWidth()-180-distance*3, this.getHeight()-4*distance, 60, 3*distance);
        draw.drawRect(this.getWidth()-180+1-distance*3, this.getHeight()-4*distance+1, 60-2, 3*distance-2);
        draw.drawString("Add", this.getWidth()-180-distance*2, this.getHeight()-2*distance);
        
        //draw remove button
        draw.setColor(Color.gray);
        draw.fillRect(this.getWidth()-240-distance*4, this.getHeight()-4*distance, 60, 3*distance);
        draw.setColor(Color.black);
        draw.drawRect(this.getWidth()-240-distance*4, this.getHeight()-4*distance, 60, 3*distance);
        draw.drawRect(this.getWidth()-240+1-distance*4, this.getHeight()-4*distance+1, 60-2, 3*distance-2);
        draw.drawString("Dlte", this.getWidth()-240-distance*3, this.getHeight()-2*distance);
            
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(Switch.switcher==0){
            for(int count = 0; count<5; count++){
                if(e.getX()>=distance+(buttonSizeX*count)+(distance*count) && e.getX()<=distance+(buttonSizeX*count)+(distance*count)+buttonSizeX){
                    if(e.getY()>=distance && e.getY()<=distance+this.getHeight()-(2*distance)){
                        Switch.chosenDay=count;
                        
                        
                        Switch.switcher=1;
                    }
                }
            }
        }
        else if(Switch.switcher==1){
            //buttons
            if(e.getX()>=this.getWidth()-distance-60 && e.getX()<=this.getWidth()-distance){
                if(e.getY()>=this.getHeight()-4*distance && e.getY()<=this.getHeight()-distance){
                    Switch.switcher = 0;
                }
            }
            else if(e.getX()>=this.getWidth()-distance-60-60-distance && e.getX()<=this.getWidth()-distance-60-distance){
                if(e.getY()>=this.getHeight()-4*distance && e.getY()<=this.getHeight()-distance){
                    Switch.grid = !Switch.grid;
                }
            }
            else if(e.getX()>=this.getWidth()-180-distance*3 && e.getX()<=this.getWidth()-120-distance*3){
                if(e.getY()>=this.getHeight()-4*distance && e.getY()<=this.getHeight()-distance){
                    Switch.addingMode = !Switch.addingMode;
                }
            }
            else if(e.getX()>=this.getWidth()-240-distance*4 && e.getX()<=this.getWidth()-180-distance*4){
                if(e.getY()>=this.getHeight()-4*distance && e.getY()<=this.getHeight()-distance){
                    removeBlock();
                }
            }
            
            //compute click-time
            else{
                int timeFrame = (this.getWidth()-distance*2);
                int hourRange = timeFrame/hourSize;
                int positionAt = e.getX()-distance;
                int timeAt = (int) ((double)positionAt/timeFrame*hourRange*60)+startHour*60;

                //removing
                for(Block i: blocks){
                    if(timeAt>=i.startsAt && timeAt<=i.startsAt+i.lengthMinutes){
                        i.active = true;
                    }
                    else{
                        i.active = false;
                    }
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
}
