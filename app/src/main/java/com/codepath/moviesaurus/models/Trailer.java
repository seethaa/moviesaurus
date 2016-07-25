package com.codepath.moviesaurus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by seetha on 7/23/16.
 */
public class Trailer {

    String name;

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    String size;
    String source;
    String type;

    public Trailer(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.size = jsonObject.getString("size");
        this.source = jsonObject.getString("source");
        this.type = jsonObject.getString("type");

    }

    public static ArrayList<Trailer> fromJSONArray(JSONArray array){
        ArrayList<Trailer> results = new ArrayList<>();

        for (int i=0; i<array.length(); i++){
            try {
                results.add(new Trailer(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
