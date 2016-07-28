/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.internal.org.objectweb.asm.Opcodes.GOTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static websitedatascrapper.JSONReader.readJsonFromUrl;

/**
 *
 * @author Nadir
 */
public class Contest {
    
    public String contest_code;
    public String contest_name;
    public ArrayList<Problems> problems;
    public String banner;
    public String rules;
    public Time time;
    public String announcements;
    public PartialScore partialscores;
    
    
    public Contest(){}
    public Contest(String contest_code,String contest_name,ArrayList<Problems> problems,String banner,
            String rules, Time time, String announcements,PartialScore partialscores )
    {
      this.contest_code=contest_code;
        this.contest_name=contest_name;
        this.problems=problems;
        this.banner=banner;
        this.rules=rules;
        this.time=time;
        this.announcements=announcements;
    }
    
    public static ArrayList<Contest> fetchContests()
    {
        ArrayList<Contest> contests= new ArrayList<Contest>();
       Document doc=null;
        boolean done=true;
        while(done)
        { try {
             doc = Jsoup.connect(urls.CONTEST_URL).get();
             done=false;
        } catch (IOException ex) {
            System.out.println("IO ERROR, Fetching contests "+" retrying..");
        }
        }
            Elements ContestsTable=doc.getElementsByClass(urls.CONTEST_CLASS);
           
          
        for(int i=0;i<ContestsTable.size();i++)    
        {
         Elements tablerow = ContestsTable.get(i).select("tbody").select("tr");
         for(int j=0;j<tablerow.size();j++){
             {
                 contests.add(fetchFromAPI(tablerow.get(j).select("td").get(0).text()));
             }
         }
        }
                    
        
        return contests;
    }
    
   private static Contest fetchFromAPI(String contest_code)
   {
      
        Document doc;
        Contest ret= new Contest();
        JSONObject json=null;
       
        System.out.println("Fetching.. "+contest_code);
        boolean done=true;
        while(done)
        { try {
           json = readJsonFromUrl(urls.CONTEST_API_URL+contest_code);
            done=false;
        } catch (IOException ex) {
            System.out.println("IO ERROR, Fetching Contest data of "+contest_code+" retrying..");
        }   catch (JSONException ex) {
             System.out.println("JSON ERROR, Fetching Contest data of "+contest_code+" retrying..");   
            }
        }
        
        try {
            ret.contest_name=JSONReader.getString(json,"name");
            ret.contest_code=JSONReader.getString(json,"code");
            ret.problems=Problems.fetchProblems(contest_code,json);
            ret.rules=cleanRules(JSONReader.getString(json,"rules"));
            ret.banner=JSONReader.getString(json,"banner");
            ret.time=Time.fetchTime(json.getJSONObject("time"));
            ret.announcements=JSONReader.getString(json,"announcements");
            ret.partialscores=PartialScore.fetchPartialScores(json.getJSONObject("partial_scores"));      
            
        } catch (JSONException ex) {
            //Logger.getLogger(Contest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      return ret;     
   }
   
   private static String cleanRules(String rules){
       Document doc=new Document(rules);
       //Elements ruleselements doc.getElementsByClass("rules-heading");
       return doc.text();
   }
    
   
}
