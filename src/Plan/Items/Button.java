package Plan.Items;

import Plan.Switch;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {
    public String title;
    public int x, y;
    public int parentWidth, parentHeight;
    public int width, height;
    public int gap;
    public EnvelopedBoolean active;
    int index;
    
    public Button(int parentWidth, int parentHeight, int index){
        title = "placeholder";
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        gap = 0;
        this.parentHeight = parentHeight;
        this.parentWidth = parentWidth;
        this.index = index;
    }
    public Button(String title, int x, int y, int width, int height, int parentHeight, int parentWidth, int index, int gap){
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parentHeight = parentHeight;
        this.parentWidth = parentWidth;
        this.index = index;
        this.gap = gap;
    }
    
    public void update(int newWidth, int newHeight){
        this.parentWidth = newWidth;
        this.parentHeight = newHeight;
    }
    
    public boolean isClicked(int clickX, int clickY){
        if(clickX>=parentWidth-x && clickX<=parentWidth-x+width){
            if(clickY>=parentHeight-y && clickY<=parentHeight-y+height){
                return true;
            }
        }
        return false;
    }
    
    public void render(Graphics2D draw){
        Font font = new Font("Arial", Font.BOLD, 15);
        draw.setFont(font);
        
        draw.setColor(Color.gray);
        try{
            if(active.active==true){
                draw.setColor(Color.gray.brighter());
            }
        }catch(NullPointerException argh){
            
        }
        draw.fillRect(parentWidth-x, parentHeight-y, width, height);
        draw.setColor(Color.black);
        draw.drawRect(parentWidth-x, parentHeight-y, width, height);
        draw.drawRect(parentWidth-x+1, parentHeight-y+1, width-2, height-2);
        draw.drawString(title, parentWidth-x+gap, parentHeight-2*gap);
    }
}
