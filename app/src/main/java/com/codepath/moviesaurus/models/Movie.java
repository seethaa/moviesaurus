package com.codepath.moviesaurus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by seetha on 7/20/16.
 */
public class Movie{
    public String getPosterPath(int orientation) {

        return String.format("https://image.tmdb.org/t/p/original/%s", backdropPath);

    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    ArrayList<Trailer> trailers;
    ArrayList<Cast> cast;
    ArrayList<String> videoURLs;
    String movieID;

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    String runTime;
    String ratings;
    String trailerID;

    public String getReleaseDate() {
        String reformattedDate = "MMM dd, yyyy";
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

             reformattedDate = formatter.format(oldFormat.parse(releaseDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedDate;
    }

    String releaseDate;
    ArrayList<Integer> genreIDs;


    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }


    public ArrayList<String> getVideoURLs() {
        return videoURLs;
    }


    public String getMovieID() {
        return movieID;
    }

    public String getFirstTrailerID(){
        return trailers.get(0).getSource();
    }


    public ArrayList<String> getAllTrailerIDs(){
        ArrayList<String> allTrailerIDs = new ArrayList<String>();
        for (int i=0; i<trailers.size(); i++){
            if (trailers.get(i).getType().equals("Trailer")){
                allTrailerIDs.add(trailers.get(i).getSource());
            }
        }
        return allTrailerIDs;
    }


    public String getRunTime() {
        return runTime;
    }




    public String getRatings() {
        return  ratings + "/10";
    }



    public Movie(JSONObject jsonObject) throws JSONException{
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.ratings = jsonObject.getString("vote_average");
        this.movieID = jsonObject.getString("id");
        this.releaseDate = jsonObject.getString("release_date");
//        this.genreJSONObject = jsonObject.getJSONObject("genre_ids");

        JSONArray genreArray = jsonObject.getJSONArray("genre_ids");
        genreIDs = new ArrayList<Integer>();
        for (int i=0; i<genreArray.length(); i++){
            int currGenreID = Integer.parseInt(genreArray.get(i).toString());
            genreIDs.add(currGenreID);
            System.out.print("DEBUGGY GENRE: " + genreArray.get(i) +",");

        }

    }

    public String getCommaSeparatedGenres(){
        String genres ="";
        for (int i=0; i<genreIDs.size(); i++){
            genres = genres + Genre.getById(genreIDs.get(i)) + ", ";
        }
        //remove last comma
        genres= genres.substring(0,genres.length()-2);
        return genres;
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){
        ArrayList<Movie> results = new ArrayList<>();

        for (int i=0; i<array.length(); i++){
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public String getCastText() {
        String castText = "";
        for (int i=0; i<cast.size(); i++){
            castText = castText + cast.get(i).getCharacter() + " ("+ cast.get(i).getName()+"), ";
        }
        if (castText.length()>2) {
            castText = castText.substring(0, castText.length() - 2);
        }

        return castText;
    }
}
