/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static websitedatascrapper.JSONReader.readJsonFromUrl;

/**
 *
 * @author Nadir
 */
public class Practice {
    public String type;
    public ArrayList<Problems> problems;
    
    public Practice(String PracticeType,int from, int to)
    {
        problems= new ArrayList<Problems>();
        this.type=PracticeType;
        Document doc=null;
        boolean done=true;
        while(done)
        { try {
             doc = Jsoup.connect(urls.PRACTICE_URL+this.type).get();
             done=false;
        } catch (IOException ex) {
            System.out.println("IO ERROR, Fetching practice "+this.type+" retrying..");
        }
        }
        
        Elements PracticeTable=doc.getElementsByClass(urls.PRACTICE_CLASS);
           
        for(int i=from;i<to&&i<PracticeTable.size();i++)    
        {
         Problems problem=new Problems();
         problem.name= PracticeTable.get(i).select("td").get(0).select("div").select("a").text();
         problem.problem_url=PracticeTable.get(i).select("td").get(0).select("a").attr("href");
         String[] s=problem.problem_url.split("/");
         problem.code=s[2];
         problem.status_url="/status/"+problem.code;
         problem.submit_url="/submit/"+problem.code;
         problem.type="0";
         problem.successful_submission=PracticeTable.get(i).select("td").get(2).select("div").text();
         problem.accuracy=PracticeTable.get(i).select("td").get(3).select("a").text();
         done=true;
         JSONObject json=null;
         
        while(done)
        { try {
           json = readJsonFromUrl(urls.PRACTICE_API_URL+problem.code);
           problem.problem_data=ProblemData.get(json);
            done=false;
        } catch (IOException ex) {
            System.out.println("IO ERROR, Fetching problem data of "+problem.code+" retrying..");
        }   catch (JSONException ex) {
             System.out.println("JSON ERROR, Fetching problem data of "+problem.code+" retrying..");   
            }
        }
        
         problems.add(problem);
    }
        
        
    }
    
    
}
