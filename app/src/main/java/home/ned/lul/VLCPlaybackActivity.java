package home.ned.lul;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import org.videolan.libvlc.media.VideoView;



import java.io.File;
import java.io.IOException;

public class VLCPlaybackActivity extends Activity {

    public final static String TAG = "LibVLCAndroid";

    DirectoryAdapter mAdapter;
    LibVLC mLibVLC = null;
//    MediaPlayer mMediaPlayer = null;

    VLCVideoView mVideoView = null;

    boolean mPlayingVideo = false; // Don't destroy libVLC if the video activity is playing.


    private Movie mSelectedMovie;

    View.OnClickListener mSimpleListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            Log.i(TAG, "hhuhuhuhuhuhuuhuhuhuhuhuhu");



            // Build the path to the media file
            String amp3 = mSelectedMovie.getVideoUrl();




            // Play the path. See the method for details.
            playMediaAtPath(mSelectedMovie);
        }
    };




    private void playMediaAtPath(Movie movie) {

        Log.i(TAG, "Mvv: "+mSelectedMovie);
        // To play with LibVLC, we need a media player object.
        // Let's get one, if needed.
//        if(mMediaPlayer == null)
//            mMediaPlayer = new MediaPlayer(mLibVLC);
//
//        // Sanity check - make sure that the file exists.
////        if(!new File(path).exists()) {
////            Toast.makeText(
////                    VLCPlaybackActivity.this,
////                    path + " does not exist!",
////                    Toast.LENGTH_LONG).show();
////            return;
////        }
//
//        // Create a new Media object for the file.
//        // Each media - a song, video, or stream is represented by a Media object for LibVLC.
//
//        Media m = new Media(mLibVLC, movie.getVideoUrl());
//        Log.i(TAG, movie.getVideoUrl());
//
//
//        // Tell the media player to play the new Media.
//        mMediaPlayer.setMedia(m);
//
//        // Finally, play it!
//        mMediaPlayer.play();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the LibVLC multimedia framework.
        // This is required before doing anything with LibVLC.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlcplayback);
        Log.i(TAG, "VLC onCreate");

        Uri vURI = null;

        Intent intent = getIntent();
        if(intent.hasExtra(DetailsActivity.MOVIE)){

            mSelectedMovie = (Movie) getIntent()
                    .getSerializableExtra(DetailsActivity.MOVIE);
            Log.i(TAG, "TriHard " + mSelectedMovie.toString());
            vURI = Uri.parse(mSelectedMovie.getVideoUrl());
        }else if(intent.hasExtra("network-stream")){
            String  url = intent.getSerializableExtra("network-stream").toString();
            vURI = Uri.parse(url);
        }



        mVideoView = (VLCVideoView) findViewById(R.id.videoViewVlc);

        Log.i(TAG,"VideoView: "+ mVideoView.toString());

        Log.i(TAG, vURI.toString());

        mVideoView.setVideoURI( vURI);
        mVideoView.start();


        Log.i(TAG, "uhuh");

    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayingVideo = false;
    };

    @Override
    public void onStop() {
        super.onStop();
//        if(!mPlayingVideo) {
//            mMediaPlayer.stop();
//            mLibVLC.release();
//            mLibVLC = null;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Setting item selected.");
                return true;
            case R.id.action_refresh:
                mAdapter.refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
