/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

/**
 *
 * @author Nadir
 */
public class ProblemData {
    
    public String contest_name;
    public String status;
    public String problem_code;
    public String problem_name;
    public String body;
    public String[] languages;
    public String max_timelimit;
    public String source_sizelimit;
    public String problem_author;
    public String problem_tester;
    public String date_added;
    
    public ProblemData(String contest_name,String status,String problem_code,String problem_name,String body,
                       String[] languages,String max_timelimit,String source_sizelimit,String problem_author,
                       String problem_tester,String date_added)
    {
        this.contest_name=contest_name;
        this.status=status;
        this.problem_code=problem_code;
        this.problem_name=problem_name;
        this.body=body;
        this.languages=languages;
        this.max_timelimit=max_timelimit;
        this.source_sizelimit=source_sizelimit;
        this.problem_author=problem_author;
        this.problem_tester=problem_tester;
        this.date_added=date_added;
    }
    
    public static ProblemData get(JSONObject json) throws JSONException
    {
        
        return new ProblemData(JSONReader.getString(json,"contest_name"),
              JSONReader.getString(json,"status"),
              JSONReader.getString(json,"problem_code"),
              JSONReader.getString(json,"problem_name"),
              JSONReader.getString(json,"body"),
              JSONReader.getString(json,"languages_supported").split(", "),
              JSONReader.getString(json,"max_timelimit"),
              JSONReader.getString(json,"source_sizelimit"),
              JSONReader.getString(json,"problem_author"),
              JSONReader.getString(json,"problem_tester"),
              JSONReader.getString(json,"date_added"));
        
    }
    
    private static String cleanBody(String body)
    {
        Document doc = new Document(body);
        return doc.text();
    }
}
