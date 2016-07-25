package com.codepath.moviesaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.moviesaurus.adapters.MovieArrayAdapter;
import com.codepath.moviesaurus.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    static ArrayList<Movie> mMoviesArrayList;
    MovieArrayAdapter movieAdapter;
    ListView mListView;
    private SwipeRefreshLayout swipeContainer;
    AsyncHttpClient client;
    MovieAPIClient movieAPIClient;

    private final String MOVIE_DATABASE_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchAllMovies();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.lvMovies);
        mMoviesArrayList = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, mMoviesArrayList);
        mListView.setAdapter(movieAdapter);

        //add on click listener to items in list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MovieActivity.this, MovieInfoActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

        //get all genres


        //fetch movie details try
        fetchAllMovies();
    }


    /**
     * Method to fetch updated data and refresh the listview.
     *
     */
    private void fetchAllMovies() {
        // Show progress bar before making network request
//        progress.setVisibility(ProgressBar.VISIBLE);
        movieAPIClient = new MovieAPIClient();
        movieAPIClient.getAllMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    mMoviesArrayList.clear();
                    mMoviesArrayList.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    printAllMovies(mMoviesArrayList);
                    Log.d("DEBUG", mMoviesArrayList.toString());
                    swipeContainer.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchMoviesOnSearch(String query) {
        // Show progress bar before making network request
//        progress.setVisibility(ProgressBar.VISIBLE);
        movieAPIClient = new MovieAPIClient();
        movieAPIClient.getMovies(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    mMoviesArrayList.clear();
                    mMoviesArrayList.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    printAllMovies(mMoviesArrayList);
                    Log.d("DEBUG", mMoviesArrayList.toString());
                    swipeContainer.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//
        });
    }


    private void printAllMovies(ArrayList<Movie> mMoviesArrayList) {
        for (int i = 0; i < mMoviesArrayList.size(); i++) {
            System.out.println("DEBUGGY MovieActivity: " + mMoviesArrayList.get(i).getOriginalTitle());
        }
    }


}
