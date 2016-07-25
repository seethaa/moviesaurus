package com.codepath.moviesaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.moviesaurus.adapters.MovieArrayAdapter;
import com.codepath.moviesaurus.models.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * MovieActivity shows list of currently playing movies with backdrop images, titles, ratings, and release date.
 * Landscape mode also shows the overview for the movie.
 */
public class MovieActivity extends AppCompatActivity {
    private ListView mListView;
    private SwipeRefreshLayout mSwipeContainer;
    static ArrayList<Movie> mMoviesArrayList;
    MovieArrayAdapter mMovieAdapter;
    MovieAPIClient mMovieAPIClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Lookup the swipe container view - pull to refresh
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call mSwipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchAllMovies();
            }
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListView = (ListView) findViewById(R.id.lvMovies);
        mMoviesArrayList = new ArrayList<>();
        mMovieAdapter = new MovieArrayAdapter(this, mMoviesArrayList);
        mListView.setAdapter(mMovieAdapter);

        //add on click listener to items in list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MovieActivity.this, MovieInfoActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

        //fetch all movies
        fetchAllMovies();
    }


    /**
     * Method to fetch updated data and refresh the listview. This method creates a new MovieAPIClient and
     * makes HTTPRequest to get a list of currently playing movies.
     *
     */
    private void fetchAllMovies() {
        mMovieAPIClient = new MovieAPIClient();
        mMovieAPIClient.getAllCurrentlyPlayingMovies(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    //get all results
                    movieJsonResults = response.getJSONArray("results");
                    mMoviesArrayList.clear(); //clear existing items from list
                    mMoviesArrayList.addAll(Movie.fromJSONArray(movieJsonResults)); //add all items to list
                    mMovieAdapter.notifyDataSetChanged(); //notify adapter
                    printAllMovies(mMoviesArrayList); //debugging purposes
                    mSwipeContainer.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//
        });
    }

    /**
     * @TODO Fetches movies based on search query
     * @param query
     */
    private void fetchMoviesOnSearch(String query) {
        mMovieAPIClient = new MovieAPIClient();
        mMovieAPIClient.getMoviesOnSearch(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    mMoviesArrayList.clear();
                    mMoviesArrayList.addAll(Movie.fromJSONArray(movieJsonResults));
                    mMovieAdapter.notifyDataSetChanged();
                    printAllMovies(mMoviesArrayList);
                    mSwipeContainer.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//
        });
    }

    /**
     * Prints list of movies for debugging.
     * @param mMoviesArrayList
     */
    private void printAllMovies(ArrayList<Movie> mMoviesArrayList) {
        for (int i = 0; i < mMoviesArrayList.size(); i++) {
            System.out.println("DEBUGGY MovieActivity: " + mMoviesArrayList.get(i).getOriginalTitle());
        }
    }


}
