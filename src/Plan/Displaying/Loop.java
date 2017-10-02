package Plan.Displaying;

import Plan.Items.Block;
import Plan.Items.Button;
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
    
    public int gap = 10;
    public int dayFrameWidth;
    public int blockHeight = 10*gap;
    public LinkedList<Block> blocks;
    public LinkedList<Button> buttons = new LinkedList();
    private int hourSize = 0;
    private int startHour = 24;
    private int endHour = 0;
    private Graphics2D draw;
    
    public void paint(Graphics g){
        draw = (Graphics2D)g;
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
    }
    
    public void addBlock(int startsAt){
        int roundingValue = 15;
        
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
    
    private void drawDaySelection(Graphics2D draw){
        dayFrameWidth = (int)(this.getWidth()-(6*gap))/5;
        //test
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK)-2;
        if(day==-1){
            day = 6;
        }
        
        Font font = new Font("Arial" , Font.PLAIN, 1);
        while(true){
            FontMetrics testMetrics = draw.getFontMetrics(font);
            if(testMetrics.stringWidth("Poniedziałek")>dayFrameWidth-2*gap){
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
            draw.drawRect(gap+(dayFrameWidth*count)+(gap*count), gap, dayFrameWidth, this.getHeight()-(2*gap));
            if(count==day){
                draw.drawRect(gap+(dayFrameWidth*count)+(gap*count)+1, gap+1, dayFrameWidth-2, this.getHeight()-(2*gap)-2);
            }
            //names
            String timeName = null;
            draw.setClip(gap+(dayFrameWidth*count)+(gap*count)+1, gap+1, dayFrameWidth-2, this.getHeight()-(2*gap)-2);
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
            int advance = ((dayFrameWidth-2*gap)-metrics.stringWidth(timeName))/2;
            draw.drawString(timeName, gap+gap+(dayFrameWidth*count)+(gap*count)+advance, 2*gap+fontHeight);   
            
            //draw calendar time

            Font font2 = new Font(font.getName(), Font.PLAIN, font.getSize()/3);
            metrics = draw.getFontMetrics(font2);
            draw.setFont(font2);
            
            if(count>0){
                cal.roll(Calendar.DATE, true);
            }
            timeName = cal.get(Calendar.DAY_OF_MONTH)+"."+cal.get(Calendar.MONTH)+"."+cal.get(Calendar.YEAR);
            advance = ((dayFrameWidth-2*gap)-metrics.stringWidth(timeName))/2;
            draw.drawString(timeName, gap+gap+(dayFrameWidth*count)+(gap*count)+advance, 2*gap+fontHeight*2);   
            
            
            
            
            draw.setClip(0, 0, this.getWidth(), this.getHeight());
        }
        cal = Calendar.getInstance();
    }

    private void drawSelectedDay(Graphics2D draw){
        
        
        draw.setColor(Color.black);
        draw.drawLine(gap, gap+3*gap, this.getWidth()-2*gap, gap+3*gap);
        draw.drawLine(gap, gap, this.getWidth()-2*gap, gap);
        
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
        hourSize = (this.getWidth()-2*gap)/(endHour-startHour);
        
        //borders
        for(int count = 0; count<(endHour-startHour);count++){
            draw.drawLine(gap+hourSize*count, gap, gap+hourSize*count, this.getHeight()-gap);
            if(Switch.grid.active){
                draw.drawLine(gap+hourSize*count+1, gap, gap+hourSize*count+1, this.getHeight()-gap);
                draw.drawLine(gap+hourSize*count-1, gap, gap+hourSize*count-1, this.getHeight()-gap);
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
            draw.setClip(0, 0, this.getWidth(), this.getHeight());

        }
        
        //draw additional grid
        if(Switch.grid.active){
            int howMuch = endHour-startHour;
            draw.setColor(Color.black);
            for(int count = 0; count<howMuch; count++){
                for(int countIns = 0; countIns<4; countIns++){
                    draw.drawLine(gap+count*hourSize+countIns*(hourSize/4), 4*gap, gap+count*hourSize+countIns*(hourSize/4), this.getHeight()-gap);
                }
            }
        }

        //draw blocks
        for(Block block: blocks){
            if(block.dayOfWeek==Switch.chosenDay){
                //set level
                block.level = 0;
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
                
                
                
                draw.setColor(Color.black);
                float mShift = (block.startsAt%60)/60f;
                mShift*=hourSize;
                int hShift = ((block.startsAt/60)-startHour)*hourSize;
                int shift = (int)mShift+hShift;
                
                int blockWidth = (int) ((block.lengthMinutes/60f)*hourSize);
                
                
                int levelShift = (blockHeight+gap)*block.level;
                
                draw.setColor(Color.gray);
                if(block.active){
                    draw.setColor(Color.gray.brighter());
                }
                draw.fillRect(gap+shift, levelShift+gap*5, blockWidth, blockHeight);
                draw.setColor(Color.black);
                draw.drawRect(gap+shift, levelShift+gap*5, blockWidth, blockHeight);
                draw.drawRect(gap+shift+1, levelShift+gap*5+1, blockWidth-2, blockHeight-2);
                
                //draw text on blocks
                //-title
                Font font = new Font("Arial", Font.BOLD, 14);
                FontMetrics metrics = draw.getFontMetrics(font);
                draw.setFont(font);
                draw.setClip(gap+shift, levelShift+gap*5, blockWidth-gap, blockHeight);
                draw.drawString(block.title, 2*gap+shift, levelShift+gap*7);
                //-teacher
                font = new Font("Arial", Font.PLAIN, 12);
                draw.setFont(font);
                metrics = draw.getFontMetrics(font);
                draw.drawString(block.teacher, 2*gap+shift, levelShift+gap*7+metrics.getHeight()+1);
                //type
                draw.drawString(block.type, 2*gap+shift, levelShift+gap*7+2*metrics.getHeight()+1);
                //place
                draw.drawString(block.place, 2*gap+shift, levelShift+gap*7+blockHeight-3*gap);
                font = new Font("Arial", Font.BOLD, 12);
                draw.setFont(font);
                draw.drawString(" "+block.room, 2*gap+shift+metrics.stringWidth(block.place), levelShift+gap*7+blockHeight-3*gap);
                
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
            draw.drawLine(gap+currTimeSize-1, gap*4, gap+currTimeSize-1, this.getHeight()-gap);
            draw.drawLine(gap+currTimeSize, gap*4, gap+currTimeSize, this.getHeight()-gap);
            draw.drawLine(gap+currTimeSize+1, gap*4, gap+currTimeSize+1, this.getHeight()-gap);
        } 
        
        drawInterface(draw);
    }
    
    private void drawInterface(Graphics2D draw){
        int buttonWidth = 100;
        
        //initialize if null
        if(buttons.isEmpty()){
            //return button
            Button button = new Button(this.getWidth(), this.getHeight(), 0);
            button.title = "Back";
            button.x = gap+buttonWidth;
            button.y = 4*gap;
            button.width = buttonWidth;
            button.height  =3*gap;
            button.gap = gap;
            buttons.add(button);
            
            //save button
            button = new Button(this.getWidth(), this.getHeight(), 1);
            button.title = "Save";
            button.x = 2*gap+2*buttonWidth;
            button.y = 4*gap;
            button.width = buttonWidth;
            button.height  = 3*gap;
            button.gap = gap;
            buttons.add(button);
            
            //grid button
            button = new Button(this.getWidth(), this.getHeight(), 2);
            button.active = Switch.grid;
            button.title = "Grid";
            button.x = 3*gap+3*buttonWidth;
            button.y = 4*gap;
            button.width = buttonWidth;
            button.height  = 3*gap;
            button.gap = gap;
            buttons.add(button);
            
            //add button
            button = new Button(this.getWidth(), this.getHeight(), 3);
            button.active = Switch.addingMode;
            button.title = "Add";
            button.x = 4*gap+4*buttonWidth;
            button.y = 4*gap;
            button.width = buttonWidth;
            button.height  = 3*gap;
            button.gap = gap;
            buttons.add(button);
            
            //delete button
            button = new Button(this.getWidth(), this.getHeight(), 4);
            button.title = "Delete";
            button.x = 5*gap+5*buttonWidth;
            button.y = 4*gap;
            button.width = buttonWidth;
            button.height  = 3*gap;
            button.gap = gap;
            buttons.add(button);
        }
        for(Button i: buttons){
            i.update(this.getWidth(), this.getHeight());
            i.render(draw);
        }
    }
    
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
                        
                        startHour = 24;
                        endHour = 0;
                        
                        Switch.switcher=1;
                    }
                }
            }
        }
        else if(Switch.switcher==1){
            //buttons
            if(buttons.get(0).isClicked(e.getX(), e.getY())){
                Switch.switcher = 0;
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
                removeBlock();
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
                
                System.out.println(tak);
                
                //setting activity
                for(Block i: blocks){
                    if(i.dayOfWeek==Switch.chosenDay){
                        if((timeAt>=i.startsAt && timeAt<=i.startsAt+i.lengthMinutes-1) && levelAt==i.level){
                            i.active = true;
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
}
