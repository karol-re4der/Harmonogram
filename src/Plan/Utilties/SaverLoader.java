package Plan.Utilties;

import Plan.Items.Block;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaverLoader {
    
     private final String directory = "";
     private String fileName = "Blocks.json";
     private File file = null;
    
    public LinkedList<Block> loadBlocks(LinkedList<Block> blocks){
        String JSONText = "";
        
        //load JSON text
        try {
            Scanner in = new Scanner(new FileReader(directory+fileName));
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
        
        for(int i = 0; i<array.length(); i++){
            Block block = new Block();
            
            //pass ints
            block.dayOfWeek = array.getJSONObject(i).getInt("dayOfWeek");
            block.startsAt = array.getJSONObject(i).getInt("startsAt");
            block.lengthMinutes = array.getJSONObject(i).getInt("length");

            
            //pass strings
            block.title = array.getJSONObject(i).getString("title");
            block.place = array.getJSONObject(i).getString("place");
            block.room = array.getJSONObject(i).getString("room");
            block.teacher = array.getJSONObject(i).getString("teacher");
            block.type = array.getJSONObject(i).getString("type");
            
            blocks.add(block);
        }
        
        
        return blocks;
    }
    public void saveBlocks(LinkedList<Block> blocks){
        //prepare JSON
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        
        for(Block i: blocks){
            JSONObject JSONBlock = new JSONObject();
            //put ints
            JSONBlock.put("dayOfWeek", i.dayOfWeek);
            JSONBlock.put("startsAt", i.startsAt);
            JSONBlock.put("length", i.lengthMinutes);
            
            //put strings
            JSONBlock.put("title", i.title);
            JSONBlock.put("place", i.place);
            JSONBlock.put("room", i.room);
            JSONBlock.put("teacher", i.teacher);
            JSONBlock.put("type", i.type);
            
            array.put(JSONBlock);
        }
        
        obj.put("array", array);
        
        //write
        try (FileWriter file = new FileWriter(directory+fileName)) {

            file.write(obj.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
