package com.codepath.moviesaurus;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

/**
 * ShowTrailerActivity uses YouTubePlayerView to play trailers for particular movie.
 */
public class ShowTrailerActivity extends YouTubeBaseActivity {
    private static final String MY_YOUTUBE_API_KEY = "AIzaSyBiTra3IzD0S_rWUefBlwduhwg53Rf21Jg";
    private String trailerSource;
    private ArrayList<String> trailerSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trailer);

        trailerSources = new ArrayList<String>();

        trailerSources = getIntent().getStringArrayListExtra("allTrailerIDs");
        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(MY_YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideos(trailerSources);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
