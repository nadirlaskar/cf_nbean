/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static websitedatascrapper.JSONReader.readJsonFromUrl;

/**
 *
 * @author Nadir
 */
public class Problems {
    public String code;
    public String name;
    public String type;
    public String successful_submission;
    public String accuracy;
    public String problem_url;
    public String submit_url;
    public String status_url;
    public ProblemData problem_data;
    
    public Problems(){}
    public Problems(String name,String code,String problem_url,String submit_url,String status_url,
                    String successful_submission,String accuracy,String type,ProblemData problem_data)
    {
        this.name=name;
        this.code=code;
        this.problem_url=problem_url;
        this.successful_submission=successful_submission;
        this.accuracy=accuracy;
        this.type=type;
        this.submit_url=submit_url;
        this.status_url=status_url;
        this.problem_data=problem_data;
    }
    
    public static ArrayList<Problems> fetchProblems(String contest_code,JSONObject json) throws JSONException
    {
       ArrayList<Problems> retProblems= new ArrayList<Problems>();
       JSONObject problems= json.getJSONObject("problems");
       Iterator keys=problems.keys();
       while(keys.hasNext())
       {
           String key=keys.next().toString();
           JSONObject pdata=null;
           String url =urls.CONTEST_API_URL+contest_code+"/problems/"+key;
        boolean done=true;
        while(done)
        { try {
           pdata=readJsonFromUrl(url);
            done=false;
        } catch (IOException ex) {
            System.out.println("IO ERROR, Fetching Problem data of "+contest_code+" retrying..");
        }   catch (JSONException ex) {
             System.out.println("JSON Error,Fetching Problem data of "+contest_code+" retrying..");   
            }
        }
           retProblems.add(getProblemObject(problems.getJSONObject(key),
                                            pdata));
       }
       
       return retProblems;
    }
    
    private static Problems getProblemObject(JSONObject ProblemJson,JSONObject ProblemDataJson) throws JSONException
    {
        ProblemData problemdata= ProblemData.get(ProblemDataJson);
        /*Problems prob= new Problems(ProblemJson.getString("name"),
                                    ProblemJson.getString("code"),
                                    ProblemJson.getString("problem_url"),
                                    ProblemJson.getString("submit_url"),
                                    ProblemJson.getString("status_url"),
                                    ProblemJson.getString("successful_submissions"),
                                    ProblemJson.getString("accuracy"),
                                    ProblemJson.getString("type"),
                                    problemdata);*/
          Problems prob= new Problems(JSONReader.getString(ProblemJson,"name"),
                                    JSONReader.getString(ProblemJson,"code"),
                                    JSONReader.getString(ProblemJson,"problem_url"),
                                    JSONReader.getString(ProblemJson,"submit_url"),
                                    JSONReader.getString(ProblemJson,"status_url"),
                                    JSONReader.getString(ProblemJson,"successful_submissions"),
                                    JSONReader.getString(ProblemJson,"accuracy"),
                                    JSONReader.getString(ProblemJson,"type"),
                                    problemdata);
        
        return prob;
        
    }
    
    
}
