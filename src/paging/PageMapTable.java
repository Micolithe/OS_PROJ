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
public class PageMapTable {

    public int realpage=-1;
    boolean inmem = false;
    public PageMapTable(){
        realpage = -1;
        inmem=false;
    }
    public void setInMemory(boolean tf){
        inmem = tf;
    }
    public boolean getInMemory(){
        return inmem;
    }
    public void setRealPage(int a){
        realpage = a;
    }
    public int getRealPage(){
        return realpage;
    }
}
