/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websitedatascrapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Nadir
 */
public class Time {
    public long start;
    public long end;
    public long current;
    public long freezing;
    
    public Time(long start,long end,long current, long freezing)
    {
        this.start=start;
        this.end=end;
        this.freezing=freezing;
    }
    
    public static Time fetchTime(JSONObject json) throws JSONException{
        return new Time(json.getLong("start"),json.getLong("end"),json.getLong("current")
                        ,json.getLong("freezing"));
    }
}
