package com.theglorious.re4der.harmonogram.Items;

import javax.swing.JPanel;

//Decorator pattern
public class BlockPanel extends JPanel{
    public int id;
    public BlockPanel(int id){
        this.id = id;
    }
}
