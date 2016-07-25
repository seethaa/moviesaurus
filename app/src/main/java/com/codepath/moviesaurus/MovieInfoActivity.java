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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * MovieInfoActivity shows details about the movie:
 * title, overview, cast, runtime, genre, trailers, and ratings.
 */
public class MovieInfoActivity extends AppCompatActivity {
    private MovieAPIClient mMovieAPIClient;
    static Movie mCurrentMovie;
    static ArrayList<Trailer> mTrailerArrayList;
    static ArrayList<Cast> mCastArrayList;

    int mPositionInMoviesList;
    int mOrientation;
    ImageView mMovieImage;
    TextView mMovieTitle;
    TextView mMovieOverview;
    TextView mMovieSummaryText;
    TextView mMovieSummaryTextLine2;
    TextView mMovieCast;
    ImageButton mPlayTrailerButton;

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
                Intent intent = new Intent(MovieInfoActivity.this, ShowTrailerActivity.class);
                intent.putStringArrayListExtra("allTrailerIDs", mCurrentMovie.getAllTrailerIDs());
                startActivity(intent);

            }

        });

        fetchMovieDetails();
        setSwipeListeners();

    }

    /**
     * @TODO: Go to next and previous moviedetail screens on swipe left and right
     */
    private void setSwipeListeners() {
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
        mTrailerArrayList = new ArrayList<Trailer>();
        mCastArrayList = new ArrayList<Cast>();

        mMovieAPIClient = new MovieAPIClient();
        mMovieAPIClient.getExtraMovieDetails(mCurrentMovie.getMovieID(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerJsonResults = null;
                JSONArray castJsonResults = null;


                try {
                    //get trailers
                    JSONObject trailerJSONObject = response.getJSONObject("trailers");
                    trailerJsonResults = trailerJSONObject.getJSONArray("youtube");
                    mTrailerArrayList.addAll(Trailer.fromJSONArray(trailerJsonResults));
                    mCurrentMovie.setTrailers(mTrailerArrayList);

                    //get cast and set to textview
                    JSONObject creditsJSONObject = response.getJSONObject("credits");
                    castJsonResults = creditsJSONObject.getJSONArray("cast");
                    mCastArrayList.addAll(Cast.fromJSONArray(castJsonResults));
                    mCurrentMovie.setCast(mCastArrayList);
                    mMovieCast.setText(mCurrentMovie.getCastText());

                    //get runtime
                    JSONObject JSONObjectRunTime = new JSONObject(String.valueOf(response));
                    String runtime = JSONObjectRunTime.getString("runtime");
                    mCurrentMovie.setRunTime(runtime);

                    //set image, title, overview, and summary lines
                    Picasso.with(getApplicationContext()).load(mCurrentMovie.getBackdropPath()).fit().placeholder(R.drawable.moviesaurusdefault).into(mMovieImage);
                    mMovieTitle.setText(mCurrentMovie.getOriginalTitle());
                    mMovieOverview.setText(mCurrentMovie.getOverview());
                    mMovieSummaryText.setText(mCurrentMovie.getRatings() + " | " + mCurrentMovie.getReleaseDate() + " | " + mCurrentMovie.getRunTime() + "min");
                    mMovieSummaryTextLine2.setText(mCurrentMovie.getCommaSeparatedGenres());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
