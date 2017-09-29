package Plan.Items;

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
    
    //optional
    public int day;
    public int month;
    public int year;
    public boolean isSingle;
    
    public Block(){
        
    }
    public Block(int day, String place, String room, String type, String teacher, int startsAt, int lengthMinutes, String title){
        this.dayOfWeek = day;
        this.place = place;
        this.place = room;
        this.type = type;
        this.teacher = teacher;
        this.lengthMinutes = lengthMinutes;
        this.title = title;
    }
    
    
    
}
