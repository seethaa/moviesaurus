package com.codepath.moviesaurus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seetha on 7/24/16.
 */
public class Cast {
    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    String character;
    String name;

    public Cast(String character, String name){
        this.character = character;
        this.name = name;
    }


    public Cast(JSONObject jsonObject) throws JSONException {
        this.character = jsonObject.getString("character");
        this.name = jsonObject.getString("name");

    }

    public static ArrayList<Cast> fromJSONArray(JSONArray array){
        ArrayList<Cast> results = new ArrayList<>();

        for (int i=0; i<10; i++){ //Get only top 10 cast members
            try {
                if (array.getJSONObject(i)!=null) {
                    results.add(new Cast(array.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
