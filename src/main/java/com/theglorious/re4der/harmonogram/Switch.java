package com.theglorious.re4der.harmonogram;

public class Switch {
    
    //singleton stuff
    private static final Switch singleton = new Switch();
    private Switch(){};
    public static Switch getInstance(){
        return singleton;
    }
    //end of singleton stuff
    
    public static int switcher = 0;             //1 - day selection, 2 - selected day
    public static int chosenDay = 0;            //active day of week
    public static boolean gridMode = false;
    public static boolean editMode = false;
    public static boolean addingMode = false;
    public static boolean erasingMode = false;
    public static final int gap = 10;
}
