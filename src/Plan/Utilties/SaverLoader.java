package Plan.Utilties;

import Plan.Items.Block;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaverLoader {
    
     private final String direction = "C:/Users/hp/Documents/Harmonogram/";
     private String fileName;
     private File file = null;
    
    public Block[] initialize(Block[] blocks, String nameOfFile){
        this.fileName = nameOfFile;
        file = new File(direction+fileName);
        int size = 0;
        if(!file.exists()){
            try{
                file.createNewFile();
            } catch(IOException argh){
                
            }
        }
         try {
             Scanner in = new Scanner(new FileReader(direction+fileName));
             if(in.hasNext()){
                 size = Integer.valueOf(in.next());
             }
             in.close();
         } catch (FileNotFoundException ex) {
             Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         blocks = new Block[size];
         for(int count = 0; count<blocks.length; count++){
             blocks[count] = new Block();
         }
         return blocks;
    }
    public Block[] loadBlocks(Block[] blocks){
        this.fileName = fileName;
        int count = 0;
        try {
             Scanner in = new Scanner(new FileReader(direction+fileName));
             while(in.hasNext()){
                 String next = in.nextLine();
                 if(next.equals("-")){
                     Block bl = new Block();
                     bl.dayOfWeek = Integer.valueOf(in.nextLine());
                     bl.place = in.nextLine();
                     bl.room = in.nextLine();
                     bl.type = in.nextLine();
                     bl.teacher = in.nextLine();
                     bl.startsAtHours = Integer.valueOf(in.nextLine());
                     bl.startsAtMinutes = Integer.valueOf(in.nextLine());
                     bl.lengthMinutes = Integer.valueOf(in.nextLine());
                     bl.title = in.nextLine();
                     blocks[count] = bl;
                     count++;
                 }
             }
             in.close();
         } catch (FileNotFoundException ex) {
             Logger.getLogger(SaverLoader.class.getName()).log(Level.SEVERE, null, ex);
         }
        return blocks;
    }
}
