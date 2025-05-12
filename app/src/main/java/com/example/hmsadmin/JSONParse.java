package com.example.hmsadmin;

import org.json.JSONObject;

public class JSONParse {
    public static String parse(JSONObject obj)
    {
        String ans="";
        try {
            ans=obj.getString("Value");
        }
        catch (Exception e){

        }
        return ans;
    }
}
