/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package paging;

/**
 *
 * @author Alexander Kronish
 */

public class PageFrame {
    
    public int owner=-1;
    public int processpage=-1;
    public boolean content=false;
    public boolean reference=false;
    public int fifoindicator=-1;
    public int cycles=0;
    
    public PageFrame(){
        owner = 0;
        content = false;
        reference = false;
        processpage = 0;
        fifoindicator = -1;
        cycles=0;
    }
    public void setPageOwner(int a){
        owner = a;
    }
    public void setLocalPage(int a){
        processpage =a;
    }
    public void setContent(boolean tf){
        content = tf;
    }
    public void setReference(boolean tf){
        reference = tf;
    }
    public int getPageOwner(){
        return owner;
    }
    public int getLocalPage(){
        return processpage;
    }
    public boolean getReference(){
        return reference;
    }
    public boolean getContent(){
        return content;
    }
    public void IncrementCycleCounter(){
        cycles++;
    }
    public void ZeroCycleCounter(){
        cycles=0;
    }
    public int getCycleCount(){
        return cycles;
    }
    public void ZeroFIFOCounter(){
        fifoindicator=0;
    }
    public void IncrementFIFOCounter(){
        fifoindicator++;
    }
    public int getFIFOCounter(){
        return fifoindicator;
    }
}


