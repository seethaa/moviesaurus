package com.codepath.moviesaurus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.moviesaurus.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieInfoActivity extends AppCompatActivity {
    int mPositionInMoviesList;
    int mOrientation;
    ImageView mMovieImage;
    TextView mMovieTitle;
    TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mOrientation = getApplicationContext().getResources().getConfiguration().orientation;


        mPositionInMoviesList = getIntent().getIntExtra("position", 0);
        Movie movie = MovieActivity.mMoviesArrayList.get(mPositionInMoviesList);

        mMovieImage = (ImageView) findViewById(R.id.ivMovieImage);
        mMovieTitle = (TextView) findViewById(R.id.tvMovieTitle);
        mMovieOverview = (TextView) findViewById(R.id.tvOverview);

        Picasso.with(getApplicationContext()).load(movie.getPosterPath(mOrientation)).fit().placeholder(R.drawable.moviesaurusdefault).into(mMovieImage);
        mMovieTitle.setText(movie.getOriginalTitle());
        mMovieOverview.setText(movie.getOverview());
    }
}
