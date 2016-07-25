package com.codepath.moviesaurus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by seetha on 7/20/16.
 * Movie objects are used to represent currently playing movies.
 */
public class Movie {
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    ArrayList<Trailer> trailers;
    ArrayList<Cast> cast;
    String movieID;
    String runTime;
    String ratings;
    String releaseDate;
    ArrayList<Integer> genreIDs;

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/original/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }


    /**
     * Formats release date from yyyy-MM-dd to MMM dd, yyyy
     *
     * @return String formatted release date
     */
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


    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public String getMovieID() {
        return movieID;
    }

    /**
     * Returns list of Trailer IDs (source) to use with YoutubePlayerAPI
     */
    public ArrayList<String> getAllTrailerIDs() {
        ArrayList<String> allTrailerIDs = new ArrayList<String>();
        for (int i = 0; i < trailers.size(); i++) {
            if (trailers.get(i).getType().equals("Trailer")) {
                allTrailerIDs.add(trailers.get(i).getSource());
            }
        }
        return allTrailerIDs;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getRatings() {
        return ratings + "/10";
    }

    public double getDoubleRatings() {
        return Double.parseDouble(ratings);
    }

    /**
     * Takes JSONObject and creates Movie objects
     *
     * @param jsonObject
     * @throws JSONException
     */
    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.ratings = jsonObject.getString("vote_average");
        this.movieID = jsonObject.getString("id");
        this.releaseDate = jsonObject.getString("release_date");

        JSONArray genreArray = jsonObject.getJSONArray("genre_ids");
        genreIDs = new ArrayList<Integer>();
        for (int i = 0; i < genreArray.length(); i++) {
            int currGenreID = Integer.parseInt(genreArray.get(i).toString());
            genreIDs.add(currGenreID);

        }

    }

    /**
     * Returns comma separated string of genres to display in summary text
     *
     * @return String formatted String with list of genres for movie
     */
    public String getCommaSeparatedGenres() {
        String genres = "";
        for (int i = 0; i < genreIDs.size(); i++) {
            genres = genres + Genre.getById(genreIDs.get(i)) + ", ";
        }
        //remove last comma
        if (genres.length() > 2) {
            genres = genres.substring(0, genres.length() - 2);
        }
        return genres;
    }

    /**
     * Parses JSONArray into a list of Movie objects
     *
     * @param array JSONArray to be parsed
     * @return ArrayList of Movie objects
     */
    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
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

    /**
     * Cast information formatted to show in MovieDetailActivity
     *
     * @return
     */
    public String getCastText() {
        String castText = "";
        for (int i = 0; i < cast.size(); i++) {
            castText = castText + cast.get(i).getCharacter() + " (" + cast.get(i).getName() + "), ";
        }
        if (castText.length() > 2) {
            castText = castText.substring(0, castText.length() - 2);
        }

        return castText;
    }
}
