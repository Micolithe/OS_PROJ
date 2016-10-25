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
public class Paging {
    public Paging(){
        //The Methods being called below accept the following arguments
        //in the following order: No. of Round Robin Cycles, No of Real Pageframes,
        //and Timeslice Size (This can be any unit of time but we can assume
        //milliseconds)
        
        
        pageLRU(1000000,16,1);
        pageLRU(1000000,16,5);
        pageLRU(1000000,16,10);
        pageLRU(1000000,16,20);
        pageLRU(1000000,16,40);
        
        pageLRU(1000000,32,1);
        pageLRU(1000000,32,5);
        pageLRU(1000000,32,10);
        pageLRU(1000000,32,20);
        pageLRU(1000000,32,40);
        
        pageLRU(1000000,64,1);
        pageLRU(1000000,64,5);
        pageLRU(1000000,64,10);
        pageLRU(1000000,64,20);
        pageLRU(1000000,64,40);
        
        pageLRU(1000000,128,1);
        pageLRU(1000000,128,5);
        pageLRU(1000000,128,10);
        pageLRU(1000000,128,20);
        pageLRU(1000000,128,40);
        
        pageFIFO(1000000,16,1);
        pageFIFO(1000000,16,5);
        pageFIFO(1000000,16,10);
        pageFIFO(1000000,16,20);
        pageFIFO(1000000,16,40);
        
        pageFIFO(1000000,32,1);
        pageFIFO(1000000,32,5);
        pageFIFO(1000000,32,10);
        pageFIFO(1000000,32,20);
        pageFIFO(1000000,32,40);
        
        pageFIFO(1000000,64,1);
        pageFIFO(1000000,64,5);
        pageFIFO(1000000,64,10);
        pageFIFO(1000000,64,20);
        pageFIFO(1000000,64,40);
        
        pageFIFO(1000000,128,1);
        pageFIFO(1000000,128,5);
        pageFIFO(1000000,128,10);
        pageFIFO(1000000,128,20);
        pageFIFO(1000000,128,40);
        
        pageRND(1000000,16,1);
        pageRND(1000000,16,5);
        pageRND(1000000,16,10);
        pageRND(1000000,16,20);
        pageRND(1000000,16,40);
        
        pageRND(1000000,32,1);
        pageRND(1000000,32,5);
        pageRND(1000000,32,10);
        pageRND(1000000,32,20);
        pageRND(1000000,32,40);
        
        pageRND(1000000,64,1);
        pageRND(1000000,64,5);
        pageRND(1000000,64,10);
        pageRND(1000000,64,20);
        pageRND(1000000,64,40); 
        
        pageRND(1000000,128,1);
        pageRND(1000000,128,5);
        pageRND(1000000,128,10);
        pageRND(1000000,128,20);
        pageRND(1000000,128,40); 
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Paging();
        //Instantiate constructor which calls all required combinations of
        //algorithms, no of frames, and timeslice sizes.
    }
    public void pageFIFO(int loopiterations, int pageframes, int timeslicesize){
        int i;
        int count = 0;
        int lasti = loopiterations;
        int pages = pageframes;
        int slice = timeslicesize;
        int currenttime = 0;
        PageFrame Frames[] = new PageFrame[pages];
        int pagemiss = 0;
        int pageins = 0;
        int pageouts = 0;
        int pagehits = 0;
        long totalcycles = 0;
        //initializes the pageframes based on the number fed to the method        
        //table structure = [process][virtualpage]

        PageMapTable[][] table = new PageMapTable[10][10];
        for (int a = 0; a<10;a++){
            for (int b = 0; b<10; b++){
                table[a][b]=new PageMapTable();
                table[a][b].setRealPage(-1);
                table[a][b].setInMemory(false);
            }
        }
        for (int c = 0; c<pages; c++){
            Frames[c]= new PageFrame();
            Frames[c].setContent(false);
            Frames[c].setPageOwner(-1);
            Frames[c].setLocalPage(-1);
            Frames[c].setReference(false);
        }
        for(i=0; i<loopiterations; i++){
            //this loop runs for the specified no of round robin cycles
            for(int currentprocess = 0; currentprocess < 10; currentprocess++){
                currenttime = 0;
                int loc=0;
                while(slice>currenttime){
                    int neededpage = getBiasedRand();
                    boolean foundopenframe = false;
                    for(int lookforblank = 0; lookforblank < pages; lookforblank++){
                        //checks each frame in real memory to see if there is one without an owner. 
                        //this is only needed in a page miss.
                        if(Frames[lookforblank].getPageOwner() == -1 && table[currentprocess][neededpage].getInMemory()==false){
                            //page in, no need to page out because frame is open
                            foundopenframe = true;
                            Frames[lookforblank].setPageOwner(currentprocess);
                            Frames[lookforblank].setLocalPage(neededpage);
                            pageins++;
                            pagemiss++;
                            currenttime+=10;
                            Frames[lookforblank].setReference(true);
                            Frames[lookforblank].setContent(contentBitGamble());
                            table[currentprocess][neededpage].setRealPage(lookforblank);
                            //System.out.println("pagein on blank");
                            table[currentprocess][neededpage].setInMemory(true);
                            Frames[lookforblank].ZeroCycleCounter();
                            Frames[lookforblank].ZeroFIFOCounter();
                            break;
                            //We found an empty frame, fill it with the current
                            //process and break out of the loop, we're done here.
                        }
                        else{
                            foundopenframe = false;
                        }
                        
                    }
                    if(foundopenframe == false){
                        if(table[currentprocess][neededpage].getInMemory() == true){
                            pagehits++;
                            currenttime++;
                            //System.out.println("pagehit");
                            //Page Hit: No paging required
                        }
                        else if(table[currentprocess][neededpage].getInMemory() == false){
                            pagemiss++;
                            //page fault! Find a candidate for removal.
                            //Victim: FIFO Counter = no. of pages.
                            for(int x = 0; x<pages;x++){
                                if(Frames[x].getFIFOCounter()>=pages){
                                    loc = x;
                                    break;
                                }
                            }
                            //pageout here
                            if(Frames[loc].getContent() == true){
                                pageouts++;
                                //System.out.println("pageout");
                                currenttime+=10;

                            }
                            //pagein here
                            int oldowner = Frames[loc].getPageOwner();
                            int oldlocalpage = Frames[loc].getLocalPage();
                            //set old owner/page to be not in memory
                            if(oldowner != -1 || oldlocalpage != -1){
                                //if the old owner is -1 it was blank, as such there
                                //is no entry in the pagemap table to update.
                                //we only need to update the pagemap if we're overwriting
                                //another process's data. In theory we should not be able to
                                //get here if we have a blank frame.
                                table[oldowner][oldlocalpage].setInMemory(false);
                            }
                            table[currentprocess][neededpage].setInMemory(true);
                            Frames[loc].setReference(true);
                            Frames[loc].setPageOwner(currentprocess);
                            Frames[loc].setLocalPage(neededpage);
                            Frames[loc].setContent(contentBitGamble());
                            //totalcycles = totalcycles + Frames[loc].getCycleCount();
                            Frames[loc].ZeroCycleCounter();
                            Frames[loc].ZeroFIFOCounter();
                            pageins++;
                            //increment counters, set reference bit, gamble on the
                            //content bit, add the no. of cycles the page was resident to
                            //the total cycle counter.
                            //System.out.println("pagein");
                            currenttime+=10;
                            for(int z = 0; z<pages;z++){
                                Frames[z].IncrementFIFOCounter();
                            }//increment fifo
                        }
                    }
                    count++;
                    for(int y = 0; y<pages;y++){
                        Frames[y].IncrementCycleCounter();
                        //System.out.println(Frames[y].getCycleCount());
                    }//increment the cycle counters
                }
            }
        }
        for(int c = 0; c<pages;c++){
            totalcycles = totalcycles + Frames[c].getCycleCount(); 
        }//after we are done, the remaining pages in memory must be totaled into
        //the cycle count total.
        System.out.println("----------FIFO----------");
        System.out.println("Timeslice Size: "+timeslicesize+" millis");
        System.out.println("Round Robin Cycles: "+lasti);
        System.out.println("No. of Real Pages: "+ pages);
        System.out.println("Page HIT: "+ pagehits);
        System.out.println("Page MISS: " + pagemiss);
        System.out.println("Page INS: " + pageins);
        System.out.println("Page OUTS: "+ pageouts);
        System.out.println("TOTAL: "+count);
        double hitratio = (double) pagehits/(double) count;
        hitratio = hitratio*100;
        System.out.println("Hit Ratio (Hits/Total): %" + hitratio);
        double avgcycles = (double) totalcycles / (double) pages;
        System.out.println("Avg Cycles a Page is Resident: "+ avgcycles);
        System.out.println(" ");
    }
     
    public void pageLRU(int loopiterations, int pageframes, int timeslicesize){
        int i;
        int count = 0;
        int lasti = loopiterations;
        int pages = pageframes;
        int slice = timeslicesize;
        int currenttime = 0;
        PageFrame Frames[] = new PageFrame[pages];
        int pagemiss = 0;
        int pageins = 0;
        int pageouts = 0;
        int pagehits = 0;
        long totalcycles = 0;
        //initializes the pageframes based on the number fed to the method        
        //table structure = [process][virtualpage]

        PageMapTable[][] table = new PageMapTable[10][10];
        for (int a = 0; a<10;a++){
            for (int b = 0; b<10; b++){
                table[a][b]=new PageMapTable();
                table[a][b].setRealPage(-1);
                table[a][b].setInMemory(false);
            }
        }
        for (int c = 0; c<pages; c++){
            Frames[c]= new PageFrame();
            Frames[c].setContent(false);
            Frames[c].setPageOwner(-1);
            Frames[c].setLocalPage(-1);
            Frames[c].setReference(false);
        }
        for(i=0; i<loopiterations; i++){
            //this loop runs for the specified no of round robin cycles
            for(int currentprocess = 0; currentprocess < 10; currentprocess++){
                currenttime = 0;
                int loc=0;
                while(slice>currenttime){
                    int neededpage = getBiasedRand();
                    boolean foundopenframe = false;
                    for(int lookforblank = 0; lookforblank < pages; lookforblank++){
                        //if we have a page miss, check if we have an open frame
                        if(table[currentprocess][neededpage].getInMemory()==false && Frames[lookforblank].getPageOwner() == -1){
                            //page in, no need to page out because frame is open
                            foundopenframe = true;
                            Frames[lookforblank].setPageOwner(currentprocess);
                            Frames[lookforblank].setLocalPage(neededpage);
                            pageins++;
                            pagemiss++;
                            currenttime+=10;
                            Frames[lookforblank].setReference(true);
                            Frames[lookforblank].setContent(contentBitGamble());
                            table[currentprocess][neededpage].setRealPage(lookforblank);
                            //System.out.println("pagein on blank");
                            table[currentprocess][neededpage].setInMemory(true);
                            Frames[lookforblank].ZeroCycleCounter();
                            break;
                        }
                        else{
                            foundopenframe = false;
                        }
                        
                    }
                    if(foundopenframe == false){
                        if(table[currentprocess][neededpage].getInMemory() == true){
                            pagehits++;
                            currenttime++;
                            //System.out.println("pagehit");
                            //Page Hit: No paging required
                        }
                        else if(table[currentprocess][neededpage].getInMemory() == false){
                            pagemiss++;
                            //page fault! Find a candidate for removal whose reference
                            //bit is false:
                            boolean foundvictim = false;
                            while(foundvictim == false){
                                loc = (int) (Math.random()*pages);
                                if(Frames[loc].getReference()==false){
                                    foundvictim = true;
                                }
                            }
                            //pageout here
                            if(Frames[loc].getContent() == true && Frames[loc].getReference() == false){
                                pageouts++;
                                //System.out.println("pageout");
                                currenttime+=10;
                            }
                            //pagein here
                            int oldowner = Frames[loc].getPageOwner();
                            int oldlocalpage = Frames[loc].getLocalPage();
                            //set old owner/page to be not in memory
                            if(oldowner != -1 || oldlocalpage != -1){
                                //if the old owner is -1 it was blank, as such there
                                //is no entry in the pagemap table to update.
                                //we only need to update the pagemap if we're overwriting
                                //another process's data. we shouldnt be able to get here
                                //unless something goes horribly wrong.
                                table[oldowner][oldlocalpage].setInMemory(false);
                            }
                            table[currentprocess][neededpage].setInMemory(true);
                            Frames[loc].setReference(true);
                            Frames[loc].setPageOwner(currentprocess);
                            Frames[loc].setLocalPage(neededpage);
                            Frames[loc].setContent(contentBitGamble());
                            //totalcycles = totalcycles + Frames[loc].getCycleCount();
                            Frames[loc].ZeroCycleCounter();
                            pageins++;
                            //increment counters, set reference bit, gamble on the content bit,
                            //and add the cycles this frame was resident to the total
                            //System.out.println("pagein");
                            currenttime+=10;
                        }
                    }
                        count++;
                        for(int y = 0; y<pages;y++){
                            Frames[y].IncrementCycleCounter();
                            //System.out.println(Frames[y].getCycleCount());
                        }
                        //here we count up the No. of Reference Bits.
                        //If the No. of RefBits is equal to the total pages
                        //we set all of them to false. This ensures we always have
                        //available frames to use.
                        int totalrefbits = 0;
                        for(int z = 0; z<pages;z++){
                            if(Frames[z].getReference()==true){
                                totalrefbits++;
                            }
                        }
                        //got total reference bits
                        if( totalrefbits == pages){
                            for(int e = 0; e<pages; e++){
                                Frames[e].setReference(false);
                                //System.out.println("Clearing all reference bits [location " + e + "].");
                            }
                        }
                    }
                
            }
        }    
        for(int c = 0; c<pages;c++){
            totalcycles = totalcycles + Frames[c].getCycleCount(); 
        }//at the end, whatever is left in memory needs its cycles added to the total
        System.out.println("----------LRU----------");
        System.out.println("Timeslice Size: "+timeslicesize+" millis");
        System.out.println("Round Robin Cycles: "+lasti);
        System.out.println("No. of Real Pages: "+ pages);
        System.out.println("Page HIT: "+ pagehits);
        System.out.println("Page MISS: " + pagemiss);
        System.out.println("Page INS: " + pageins);
        System.out.println("Page OUTS: "+ pageouts);
        System.out.println("TOTAL: "+count);
        double hitratio = (double) pagehits/(double) count;
        hitratio = hitratio*100;
        System.out.println("Hit Ratio (Hits/Total): %" + hitratio);
        double avgcycles = (double) totalcycles / (double) pages;
        System.out.println("Avg Cycles a Page is Resident: "+ avgcycles);
        System.out.println(" ");
    
    }
    
    public void pageRND(int loopiterations, int pageframes, int timeslicesize){
        int i;
        int count = 0;
        int lasti = loopiterations;
        int pages = pageframes;
        int slice = timeslicesize;
        int currenttime = 0;
        PageFrame Frames[] = new PageFrame[pages];
        int pagemiss = 0;
        int pageins = 0;
        int pageouts = 0;
        int pagehits = 0;
        long totalcycles = 0;
        //initializes the pageframes based on the number fed to the method
        //table structure = [process][virtualpage]
        PageMapTable[][] table = new PageMapTable[10][10];
        for (int a = 0; a<10;a++){
            for (int b = 0; b<10; b++){
                table[a][b]=new PageMapTable();
                table[a][b].setRealPage(-1);
                table[a][b].setInMemory(false);
            }
        }
        for (int c = 0; c<pages; c++){
            Frames[c]= new PageFrame();
            Frames[c].setContent(false);
            Frames[c].setPageOwner(-1);
            Frames[c].setLocalPage(-1);
            Frames[c].setReference(false);
        }
        
        for(i=0; i<loopiterations; i++){
            //System.out.println("cycles");
            for(int currentprocess = 0; currentprocess < 10; currentprocess++){
                //System.out.println("round robin");
                currenttime = 0;
                int loc=0;
                boolean foundopenframe = false;
                while(slice>currenttime){
                    
                    int neededpage = getBiasedRand();
                    for(int lookforblank = 0; lookforblank < pages; lookforblank++){ 
                        //check for frame not in real memory & we have an open one
                        if(table[currentprocess][neededpage].getInMemory()==false && Frames[lookforblank].getPageOwner() == -1){
                            //page in, no need to page out because frame is open
                            foundopenframe = true;
                            Frames[lookforblank].setPageOwner(currentprocess);
                            Frames[lookforblank].setLocalPage(neededpage);
                            pageins++;
                            pagemiss++;
                            currenttime+=10;
                            Frames[lookforblank].setReference(true);
                            Frames[lookforblank].setContent(contentBitGamble());
                            table[currentprocess][neededpage].setRealPage(lookforblank);
                            //System.out.println("pagein on blank");
                            table[currentprocess][neededpage].setInMemory(true);
                            Frames[lookforblank].ZeroCycleCounter();
                            Frames[lookforblank].setContent(contentBitGamble());
                            break;
                        }
                        else{
                            foundopenframe = false;
                        }
                    }
                    
                    if(foundopenframe == false){
                        if(table[currentprocess][neededpage].getInMemory() == true){
                            pagehits++;
                            currenttime++;
                            loc = table[currentprocess][neededpage].getRealPage();
                            Frames[loc].setContent(contentBitGamble());
                            //System.out.println("pagehit");
                        }
                        else if(table[currentprocess][neededpage].getInMemory() == false){
                            pagemiss++;
                            //PageFault! Because this is random the reference bit
                            //is irrelevant
                            loc = (int) (Math.random()*pages);
                            //pageout here
                            if(Frames[loc].getContent() == true){
                                pageouts++;
                                //System.out.println("pageout");
                                currenttime+=10;
                            }
                            //pagein here
                            int oldowner = Frames[loc].getPageOwner();
                            int oldlocalpage = Frames[loc].getLocalPage();
                            if(oldowner != -1 && oldlocalpage != -1){
                                //if page owner is -1, page is blank and there
                                //is no corresponding entry to update.
                                table[oldowner][oldlocalpage].setInMemory(false);
                            }
                            table[currentprocess][neededpage].setInMemory(true);
                            table[currentprocess][neededpage].setRealPage(loc);
                            Frames[loc].setReference(true);
                            Frames[loc].setPageOwner(currentprocess);
                            Frames[loc].setLocalPage(neededpage);
                            Frames[loc].setContent(contentBitGamble());
                            //totalcycles = totalcycles + Frames[loc].getCycleCount();
                            Frames[loc].ZeroCycleCounter();
                            pageins++;
                            //increment counters, add up total cycles, set reference
                            //System.out.println("pagein");
                            currenttime+=10;
                        }
                    }
                    count++;
                    for(int y = 0; y<pages;y++){
                        Frames[y].IncrementCycleCounter(); 
                    }//increment cycle counter on all frames
                } 
            }
        }
        for(int c = 0; c<pages;c++){
            totalcycles = totalcycles + Frames[c].getCycleCount(); 
        }//at the end add data from remaining frames to totalcycles
        System.out.println("----------RNDM----------");
        System.out.println("Timeslice Size: "+timeslicesize+" millis");
        System.out.println("Round Robin Cycles: "+lasti);
        System.out.println("No. of Real Pages: "+ pages);
        System.out.println("Page HIT: "+ pagehits);
        System.out.println("Page MISS: " + pagemiss);
        System.out.println("Page INS: " + pageins);
        System.out.println("Page OUTS: "+ pageouts);
        System.out.println("TOTAL: "+count);
        double hitratio = (double) pagehits/(double) count;
        hitratio = hitratio*100;
        System.out.println("Hit Ratio (Hits/Total): %" + hitratio);
        //System.out.println(totalcycles);
        double avgcycles = (double) totalcycles / (double) pages;
        System.out.println("Avg Cycles a Page is Resident: "+ avgcycles);
        System.out.println(" ");
    }


    public boolean contentBitGamble(){
        int num = (int) (Math.random()*10);
        boolean returnval;
        switch(num){
            //20 Percent Chance of True
            case 1: case 2:{
                returnval = true;
                break;
            }
            default:{
                //80 Percent Chance of False
                returnval = false;
                break;
            }
        }
        return returnval;
    }
        
        

    
    public int getBiasedRand(){
        //this method taken from project PDF
        int biasednum = 0;
        int num = (int) (Math.random()* 10 );
        if (num >= 9) // 10 percent area
            {
            num = (int) (Math.random()* 8 );
            biasednum = num;
            }
        else  // 90 percent area
            {
            num = (int) (Math.random()* 2 );
            if (num == 0) 
            { 
                biasednum = 8;
            }
            else
            {
                biasednum = 9;
            }
        }
        return biasednum;
    }
   

}
