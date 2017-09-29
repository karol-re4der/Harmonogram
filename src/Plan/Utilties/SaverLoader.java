package Plan.Utilties;

import Plan.Items.Block;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaverLoader {
    
     private final String direction = "";
     private String fileName = "Blocks.json";
     private File file = null;
    
    public Block[] initialize(Block[] blocks){
        
        file = new File(direction+fileName);
        int size = 0;
        
        //load JSON text
        String JSONText = "";
        try {
            Scanner in = new Scanner(new FileReader(direction+fileName));
            while(in.hasNext()){
                JSONText += in.next();
            }

            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //turn JSON text into an object
        JSONObject obj = new JSONObject(JSONText);
        JSONArray array = obj.getJSONArray("array");
        
        //initialize array
        blocks = new Block[array.length()];
        for(int count = 0; count<blocks.length; count++){
            blocks[count] = new Block();
        }
        return blocks;
    }
    public Block[] loadBlocks(Block[] blocks){
        String JSONText = "";
        
        //load JSON text
        try {
            Scanner in = new Scanner(new FileReader(direction+fileName));
            while(in.hasNext()){
                JSONText += in.next();
            }
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //turn into an object
        JSONObject obj = new JSONObject(JSONText);
        JSONArray array = obj.getJSONArray("array");
        
        for(int i = 0; i<blocks.length; i++){
            //pass ints
            blocks[i].dayOfWeek = array.getJSONObject(i).getInt("dayOfWeek");
            blocks[i].startsAt = array.getJSONObject(i).getInt("startsAt");
            blocks[i].lengthMinutes = array.getJSONObject(i).getInt("length");

            
            //pass strings
            blocks[i].title = array.getJSONObject(i).getString("title");
            blocks[i].place = array.getJSONObject(i).getString("place");
            blocks[i].room = array.getJSONObject(i).getString("room");
            blocks[i].teacher = array.getJSONObject(i).getString("teacher");
            blocks[i].type = array.getJSONObject(i).getString("type");
        }
        
        
        return blocks;
    }
}
