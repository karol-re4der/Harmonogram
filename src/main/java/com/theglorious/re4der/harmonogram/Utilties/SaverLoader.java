package com.theglorious.re4der.harmonogram.Utilties;

import com.theglorious.re4der.harmonogram.Items.Block;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;



public class SaverLoader {
    
     private final String directory = "";
     private String fileName = "Blocks.json";
     private File file = null;
    
    private void createIfNone(){

        File file2 = new File(directory+fileName);
        if(!file2.exists()){
            try {
                file2.createNewFile();
                FileWriter writer = new FileWriter(directory+fileName);
                writer.write("{\"array\":[]}");
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     
    public LinkedList<Block> loadBlocks(LinkedList<Block> blocks){
        String JSONText = "";

        //create if doesn't exist
        createIfNone();
        
        //load JSON text
        try {
            Scanner in = new Scanner(new FileReader(directory+fileName));
            while(in.hasNext()){
                JSONText += in.nextLine();
            }
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //turn into an object
        JSONObject obj = new JSONObject(JSONText);
        JSONArray array = obj.getJSONArray("array");
        
        int id = 0;
        
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
            
            //set id
            for(int ii = 0; ii<blocks.size(); ii++){
                if(blocks.get(ii).id==id){
                    id++;
                    ii = 0;
                }
            }
            block.id = id;
            id++;
            
            blocks.add(block);
        }
        
        
        return blocks;
    }
    public void saveBlocks(LinkedList<Block> blocks){
        //create if doesn't exist
        createIfNone();
        
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
