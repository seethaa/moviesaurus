package com.codepath.moviesaurus.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by seetha on 7/23/16.
 * MovieAPIClient handles all network calls to server to get data from the MovieDatabase API
 */
public class MovieAPIClient {
    private AsyncHttpClient mClient;

    private final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";

    public MovieAPIClient() {
        this.mClient = new AsyncHttpClient();
    }

    private String getAPIUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl + "api_key=" + API_KEY;
    }

    /**
     * @TODO: Method for accessing the search API
     */
    public void getMoviesOnSearch(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getAPIUrl("now_playing?");
            mClient.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * API Call to get all currently playing movies
     *
     * @param handler
     */
    public void getAllCurrentlyPlayingMovies(JsonHttpResponseHandler handler) {
        String url = getAPIUrl("now_playing?");
        mClient.get(url, handler);
    }

    /**
     * API Call to get extra details about a particular movie using the movie ID
     */
    public void getExtraMovieDetails(String movieID, JsonHttpResponseHandler handler) {
        String url = getAPIUrl(movieID + "?");
        url = url + "&append_to_response=similar_movies,alternative_titles,keywords,releases,trailers,credits";
        mClient.get(url, handler);
    }


}
