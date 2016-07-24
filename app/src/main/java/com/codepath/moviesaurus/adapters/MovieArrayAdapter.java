package com.codepath.moviesaurus.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.moviesaurus.R;
import com.codepath.moviesaurus.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by seetha on 7/20/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    int mOrientation;
    //View lookup cache
    private static class ViewHolder {
        ImageView movieImage;
        TextView movieTitle;
        TextView mMovieSubtext;

    }

    //View lookup cache for landscape
    private static class ViewHolderLandscape {
        ImageView movieImage;
        TextView movieTitle;
        TextView movieOverview;
        TextView mMovieSubtext;

    }
    public MovieArrayAdapter(Context context,List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mOrientation = getContext().getResources().getConfiguration().orientation;

        //get the data item for position
        Movie movie = getItem(position);

        ViewHolder viewHolder; //view lookup cache stored in tag
        ViewHolderLandscape viewHolderLandscape;

        //landscape mode
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            //check that the existing view is being reused
            if (convertView == null) {//if null, inflate layout

                viewHolderLandscape = new ViewHolderLandscape();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_movie_landscape, parent, false);

                viewHolderLandscape.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                //clear out image from convertView
                //viewHolder.movieImage.setImageResource(0);

                viewHolderLandscape.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);
                viewHolderLandscape.movieOverview = (TextView) convertView.findViewById(R.id.tvOverview);

                viewHolderLandscape.mMovieSubtext = (TextView) convertView.findViewById(R.id.tvSummaryText);
                convertView.setTag(viewHolderLandscape);

                //populate data
                viewHolderLandscape.movieTitle.setText(movie.getOriginalTitle());
                viewHolderLandscape.movieOverview.setText(movie.getOverview());
                viewHolderLandscape.mMovieSubtext.setText(movie.getRatings());

                Picasso.with(getContext()).load(movie.getPosterPath(mOrientation)).fit().placeholder(R.drawable.moviesaurusdefault).into(viewHolderLandscape.movieImage);


            } else {
                viewHolderLandscape = (ViewHolderLandscape) convertView.getTag();

            }
        }

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT || mOrientation==Configuration.ORIENTATION_UNDEFINED) {//portrait mode

            //check that the existing view is being reused
            if (convertView == null) {//if null, inflate layout
              viewHolder = new ViewHolder();
              LayoutInflater inflater = LayoutInflater.from(getContext());
              convertView = inflater.inflate(R.layout.item_movie, parent, false);

              viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
              //clear out image from convertView
              //viewHolder.movieImage.setImageResource(0);

              viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.tvMovieTitle);
              // viewHolder.movieOverview = (TextView) convertView.findViewById(R.id.tvOverview);

              viewHolder.mMovieSubtext = (TextView) convertView.findViewById(R.id.tvSummaryText);
              convertView.setTag(viewHolder);

              //populate data
              viewHolder.movieTitle.setText(movie.getOriginalTitle());
              // viewHolder.movieOverview.setText(movie.getOverview());
              viewHolder.mMovieSubtext.setText(movie.getRatings());

              Picasso.with(getContext()).load(movie.getPosterPath(mOrientation)).fit().placeholder(R.drawable.moviesaurusdefault).into(viewHolder.movieImage);

          }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }


        return convertView;
    }
}
