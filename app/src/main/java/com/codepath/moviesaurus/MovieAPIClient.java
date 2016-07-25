package com.codepath.moviesaurus;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by seetha on 7/23/16.
 */
public class MovieAPIClient {
    private final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private AsyncHttpClient client;

    private final String MOVIE_DATABASE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
//    String trailerURL = "https://api.themoviedb.org/3/movie/" + mCurrentMovie.getMovieID() + "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    public MovieAPIClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl + "api_key=" + API_KEY;
    }

    // Method for accessing the search API
    public void getMovies(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("now_playing?");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getAllMovies(JsonHttpResponseHandler handler) {
        String url = getApiUrl("now_playing?");
//       client.get(url + movieID + ".json", handler);
        String trailerURL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        client.get(url, handler);
    }

    // Method for accessing books API to get publisher and no. of pages in a book.
    public void getExtraMovieDetails(String movieID, JsonHttpResponseHandler handler) {
        String url = getApiUrl(movieID+"?");
        url = url + "&append_to_response=similar_movies,alternative_titles,keywords,releases,trailers,credits";
//        String trailerURL = "https://api.themoviedb.org/3/movie/" + movieID + "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        client.get(url, handler);
    }

//    public void getAllGenres( JsonHttpResponseHandler handler) {
//        String url = getApiUrl("genre/movie/list?");
//        client.get(url, handler);
//    }



    /*public void getCurrentlyPlayingMovies(JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams("api_key", API_KEY);
        client.get(MOVIE_DATABASE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    mMoviesArrayList.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", mMoviesArrayList.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }*/


}
