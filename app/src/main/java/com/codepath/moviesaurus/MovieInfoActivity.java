package com.codepath.moviesaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.moviesaurus.models.Cast;
import com.codepath.moviesaurus.models.Movie;
import com.codepath.moviesaurus.models.Trailer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieInfoActivity extends AppCompatActivity {
    int mPositionInMoviesList;
    int mOrientation;
    ImageView mMovieImage;
    TextView mMovieTitle;
    TextView mMovieOverview;
    TextView mMovieSummaryText;
    TextView mMovieSummaryTextLine2;
    TextView mMovieCast;
    ImageButton mPlayTrailerButton;
    static Movie mCurrentMovie;
    private MovieAPIClient mMovieAPIClient;
    static ArrayList<Trailer> trailerArrayList;
    static ArrayList<Cast> castArrayList;

    AsyncHttpClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mOrientation = getApplicationContext().getResources().getConfiguration().orientation;


        mPositionInMoviesList = getIntent().getIntExtra("position", 0);
        mCurrentMovie = MovieActivity.mMoviesArrayList.get(mPositionInMoviesList);

        mMovieImage = (ImageView) findViewById(R.id.ivMovieImage);
        mMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        mMovieOverview = (TextView) findViewById(R.id.tvOverview);


        mPlayTrailerButton = (ImageButton) findViewById(R.id.ibPlayTrailer);

        mMovieSummaryText = (TextView) findViewById(R.id.tvSummaryText);
        mMovieSummaryTextLine2 = (TextView) findViewById(R.id.tvSummaryTextLine2);
        mMovieCast = (TextView) findViewById(R.id.tvCast);

        mPlayTrailerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//
//                Toast.makeText(MovieInfoActivity.this,
//                        "ImageButton is clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MovieInfoActivity.this, ShowTrailerActivity.class);
                intent.putStringArrayListExtra("allTrailerIDs", mCurrentMovie.getAllTrailerIDs());
                startActivity(intent);

            }

        });
        fetchMovieDetails();



        //set right swipe listener
        View currView = this.findViewById(android.R.id.content);

        currView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                //do nothing
                Toast.makeText(MovieInfoActivity.this, "Down", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {//go to next movie in list

//                Toast.makeText(MovieInfoActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeUp() {
                //do nothing
                Toast.makeText(MovieInfoActivity.this, "Up", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {//go to last movie in list
//                Toast.makeText(MovieInfoActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void fetchMovieDetails() {
        trailerArrayList = new ArrayList<Trailer>();
        castArrayList = new ArrayList<Cast>();

        // Show progress bar before making network request
//        progress.setVisibility(ProgressBar.VISIBLE);
        mMovieAPIClient = new MovieAPIClient();
        mMovieAPIClient.getExtraMovieDetails(mCurrentMovie.getMovieID(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerJsonResults = null;
                JSONArray castJsonResults = null;


                try {
                    //get trailers
                    System.out.println("DEBUGGY extra: " + response);
                    JSONObject trailerJSONObject = response.getJSONObject("trailers");
                    trailerJsonResults = trailerJSONObject.getJSONArray("youtube");
                    trailerArrayList.addAll(Trailer.fromJSONArray(trailerJsonResults));
                    printTrailerList(trailerArrayList);
                    mCurrentMovie.setTrailers(trailerArrayList);

                    //get cast and set to textview
                    JSONObject creditsJSONObject = response.getJSONObject("credits");
                    System.out.println("DEBUGGY CAST: "+creditsJSONObject);
                    castJsonResults = creditsJSONObject.getJSONArray("cast");
                    castArrayList.addAll(Cast.fromJSONArray(castJsonResults));
                    printCastList(castArrayList);
                    mCurrentMovie.setCast(castArrayList);
                    mMovieCast.setText(mCurrentMovie.getCastText());

                    //get runtime
                    JSONObject JSONObjectRunTime = new JSONObject(String.valueOf(response));
                    String runtime =  JSONObjectRunTime.getString("runtime");

                    mCurrentMovie.setRunTime(runtime);

                    Picasso.with(getApplicationContext()).load(mCurrentMovie.getPosterPath(mOrientation)).fit().placeholder(R.drawable.moviesaurusdefault).into(mMovieImage);
                    mMovieTitle.setText(mCurrentMovie.getOriginalTitle());
                    mMovieOverview.setText(mCurrentMovie.getOverview());
                    mMovieSummaryText.setText(mCurrentMovie.getRatings() + " | "  + mCurrentMovie.getReleaseDate() + " | " + mCurrentMovie.getRunTime() + "min");
                    mMovieSummaryTextLine2.setText(mCurrentMovie.getCommaSeparatedGenres());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void printCastList(ArrayList<Cast> castArrayList) {
        for (int i=0; i<castArrayList.size(); i++){
            System.out.println("DEBUGGY CAST: " + castArrayList.get(i).getName());
        }
    }


    private void printTrailerList(ArrayList<Trailer> trailerArrayList) {
        for (int i=0; i<trailerArrayList.size(); i++){
            System.out.println("DEBUGGY: " + trailerArrayList.get(i).getName());
        }

    }
}
