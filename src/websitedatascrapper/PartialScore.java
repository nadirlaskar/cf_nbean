/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Nadir
 */
public class PartialScore {
    
    public HashMap<String,Score> scores;

    
    public PartialScore()
    {
       this.scores=new HashMap();
    }
    
    
    public static PartialScore fetchPartialScores(JSONObject json) throws JSONException
    {
        PartialScore pscore= new PartialScore();
        Iterator keys=json.keys();
        while(keys.hasNext())
        {
            String key=keys.next().toString();
            JSONArray score = json.getJSONArray(key);
            pscore.add(key,score);
        }
        return pscore;
    }
    
    public void add(String problem_code,JSONArray score)
    {
        scores.put(problem_code,new Score(score));
    }
    
    public JSONArray getscoresof(String problem_code)
    {
        return scores.get(problem_code).score;
    }

    private static class Score {
         JSONArray score;
        public Score(JSONArray score) {
            this.score=score;
        }
    }
}



