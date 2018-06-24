
import com.theglorious.re4der.harmonogram.Displaying.Loop;
import com.theglorious.re4der.harmonogram.Items.Block;
import java.util.LinkedList;
import java.util.Random;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

public class TestClass {
    @Test
    public void testPlaceHolder(){
        Assert.assertEquals(true, true);
    }
    
    @Test
    public void testIDSystem(){
        ////Test a logic of adding/removing new blocks, specifically ID verifying system.
        //set framework
        int testSize = 10;
        Loop loop = new Loop();
        loop.blocks = new LinkedList();
        
        //add random blocks
        for(int i = 0; i<testSize; i++)
            loop.addBlock(60);
        
        //verify ids #1
        int errs = 0;
        for(Block bl: loop.blocks){
            for(Block inbl: loop.blocks){
                System.out.println(bl.id+";"+inbl.id);
                if(!bl.equals(inbl)){
                    if(bl.id==inbl.id){
                        errs+=1;
                    }
                }
            }
        }
        Assert.assertEquals("Mismatched IDs at first step", 0, errs);
        
        //remove random blocks
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for(int i = 0; i<testSize/2; i++){
            loop.removeBlock(loop.blocks.get(rand.nextInt(loop.blocks.size())).id);
        }
        
        //verify ids #2
        errs = 0;
        for(Block bl: loop.blocks){
            for(Block inbl: loop.blocks){
                if(!bl.equals(inbl)){
                    if(bl.id==inbl.id){
                        errs+=1;
                    }
                }
            }
        }
        Assert.assertEquals("Mismatched IDs at second step", 0, errs);
            
        //add more random blocks
        for(int i = 0; i<testSize/2; i++)
            loop.addBlock(60);
        
        //verify ids #3
        errs = 0;
        for(Block bl: loop.blocks){
            for(Block inbl: loop.blocks){
                if(!bl.equals(inbl)){
                    if(bl.id==inbl.id){
                        errs+=1;
                    }
                }
            }
        }
        Assert.assertEquals("Mismatched IDs at third step", 0, errs);
    }
}
