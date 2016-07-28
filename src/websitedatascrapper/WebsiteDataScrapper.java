/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nadir
 */
public class WebsiteDataScrapper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
       // ArrayList<Contest> contests=Contest.fetchContests();
        Practice practice = new Practice("school",0,2);
        Iterator i;
        i=practice.problems.iterator();
        while(i.hasNext())
        {
            Problems problem=(Problems)i.next();
            System.out.println("Problem Name "+problem.name+"\t Problem Code "+problem.code);
            
              
        }
            
        
        System.out.println("Done");
        
        
    }
    
}
