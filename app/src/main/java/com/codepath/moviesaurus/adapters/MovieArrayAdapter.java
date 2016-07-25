package com.codepath.moviesaurus.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.moviesaurus.R;
import com.codepath.moviesaurus.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by seetha on 7/20/16.
 * MovieArrayAdapter uses a ViewHolder to update views in landscape and portrait view for MovieActivity.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    int mOrientation;

    //View lookup cache
    private static class ViewHolder {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieSubtext;
        TextView movieOverview;
        RatingBar movieRatingBar;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mOrientation = getContext().getResources().getConfiguration().orientation;

        //get the data item for position
        Movie movie = getItem(position);

        ViewHolder viewHolder; //view lookup cache stored in tag

        //check that the existing view is being reused
        if (convertView == null) {//if null, inflate layout
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {

                convertView = inflater.inflate(R.layout.item_movie_landscape, parent, false);
                viewHolder.movieOverview = (TextView) convertView.findViewById(R.id.tvOverview);

            } else {//portrait mode
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
            }
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            //clear out image from convertView
            viewHolder.movieImage.setImageResource(0);

            viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);

            viewHolder.movieRatingBar = (RatingBar) convertView.findViewById(R.id.rbMovieRatings);
            viewHolder.movieSubtext = (TextView) convertView.findViewById(R.id.tvSummaryText);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        //populate movie title
        viewHolder.movieTitle.setText(movie.getOriginalTitle());

        //if landscape mode, add overview text
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewHolder.movieOverview.setText(movie.getOverview());
        }
            viewHolder.movieRatingBar.setRating(Float.parseFloat(movie.getDoubleRatings()+""));

        viewHolder.movieSubtext.setText(movie.getRatings() + " | " + movie.getReleaseDate());
        Picasso.with(getContext()).load(movie.getBackdropPath()).fit().placeholder(R.drawable.moviesaurusdefault).into(viewHolder.movieImage);

        return convertView;
    }
}
