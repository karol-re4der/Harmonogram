
import com.theglorious.re4der.harmonogram.Displaying.Loop;
import com.theglorious.re4der.harmonogram.Items.Block;
import com.theglorious.re4der.harmonogram.Switch;
import com.theglorious.re4der.harmonogram.Utilties.SaverLoader;
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
        int testSize = 100;
        Loop loop = new Loop();
        loop.blocks = new LinkedList();
        
        //add random blocks
        for(int i = 0; i<testSize; i++)
            loop.addBlock(60);
        
        //verify ids #1
        int errs = 0;
        for(Block bl: loop.blocks){
            for(Block inbl: loop.blocks){
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
                        System.out.println(bl.id+" "+inbl.id);
                        errs+=1;
                    }
                }
            }
        }
        Assert.assertEquals("Mismatched IDs at third step", 0, errs);
    }

    @Test
    public void testSingleton(){
        //set test framework
        Switch singleton1 = Switch.getInstance();
        Switch singleton2 = Switch.getInstance();
        
        //change some value
        singleton1.editMode = !singleton1.editMode;
        
        //check
        Assert.assertEquals(true, singleton1.editMode==singleton2.editMode);
    }

    @Test
    public void testSaverLoader(){
        int testSize = 10;
        
        //set up test framework
        SaverLoader sl = new SaverLoader();
        sl.switchFile("testBlocks.json");
        LinkedList<Block> blocks = new LinkedList();
        
        //add some blocks
        for(int i = 0; i<testSize; i++){
            Block block = new Block();
            block.dayOfWeek = i%5;
            block.startsAt=60;
            block.lengthMinutes=60;
            block.title="placeholder";
            block.teacher="placeholder";
            block.type="placeholder";
            block.place="placeholder";
            block.room="placeholder";
            block.setID(blocks);
            blocks.add(block);
            blocks.get(i).setID(blocks);
        }
        
        //save blocks
        sl.saveBlocks(blocks);
        
        //load blocks
        LinkedList<Block> loadedBlocks = new LinkedList();
        loadedBlocks = sl.loadBlocks(loadedBlocks);
        
        //compare
        int fits = 0;
        int expected = 2*testSize;
        for(Block bl: blocks){
            for(Block inbl: loadedBlocks){
                if(bl.equals(inbl)){
                    fits+=1;
                }
            }
        }
        Assert.assertEquals(expected, fits);
    }
    
    @Test
    public void testBubbleSort(){
        //set test framework
        int testSize = 10;
        Loop loop = new Loop();
        loop.blocks = new LinkedList();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        
        //add random blocks
        for(int i = 0; i<testSize; i++){
            loop.addBlock(rand.nextInt(23*60));
        }

        //sort it out
        loop.bubbleSort();

        //check the new order
        int errs = 0;
        for(int i = 1; i<loop.blocks.size(); i++){
            if(loop.blocks.get(i).startsAt<loop.blocks.get(i-1).startsAt){
                errs+=1;
            }
        }
        Assert.assertEquals(0, errs);
    }
}