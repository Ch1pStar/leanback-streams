package home.ned.lul;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import java.lang.ref.WeakReference;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.media.VideoView;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.AndroidUtil;
import org.videolan.libvlc.util.HWDecoderUtil;

import org.videolan.libvlc.util.MediaBrowser;


//import org.videolan.vlc.media.MediaUtils;



public class VLCVideoView extends SurfaceView
        implements IVLCVout.Callback{

    private static final String TAG = "VLCVideoView";

    private static LibVLC sLibVLC;
    private Media mMedia;
    private MediaPlayer mMediaPlayer;


    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    public VLCVideoView(Context context) {
        super(context);
    }
    public VLCVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public VLCVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VLCVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: NO WAY");
        MediaPlayer.TrackDescription[] tracks = mMediaPlayer.getSpuTracks();
        Log.i(TAG, "touch: sub tracks: "+tracks);
        return super.onTouchEvent(event);
    }

    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {
        Log.d(TAG, "onSurfacesDestroyed: DESTROYED");
        if(mMediaPlayer instanceof MediaPlayer){
            mMediaPlayer.stop();
        }

    }

    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout) {
        Log.d(TAG, "onHardwareAccelerationError: " + vlcVout.toString());
    }


    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<VLCVideoView> mOwner;

        public MyPlayerListener(VLCVideoView owner) {
            mOwner = new WeakReference<VLCVideoView>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            MediaPlayer player = mOwner.get().mMediaPlayer;
            switch(event.type) {
                case MediaPlayer.Event.Vout:
                    MediaPlayer.TrackDescription[] tracks = player.getSpuTracks();
                    if(tracks != null){
                        for (MediaPlayer.TrackDescription track : tracks){
                            Log.i(TAG, "track: "+track.name+" "+track.id);
                            if(track.id > 0){
                                player.setSpuTrack(track.id);
                            }
                        }
                    }
                    break;
                case MediaPlayer.Event.EndReached:
                    Log.d(TAG, "MediaPlayerEndReached");
                    player.release();
                    break;
                case MediaPlayer.Event.Playing:
                    Log.d(TAG, "onEvent: PLAYING");
                    break;
                case MediaPlayer.Event.Paused:
                    Log.d(TAG, "onEvent: PAUSED");
                    break;
                case MediaPlayer.Event.Stopped:
                    Log.d(TAG, "onEvent: STOPPED");
                    break;
                default:
                    break;
            }
        }
    }



    private void initLibVLC() {
        sLibVLC = new LibVLC();
    }

    public void setVideoURI(Uri uri) {
        initLibVLC();

        mMedia = new Media(sLibVLC, uri);
        mMedia.setHWDecoderEnabled(true, false);
        mMedia.addOption(":network-caching=1000");
        mMedia.addOption(":preferred-resolution=-1");


        Log.d(TAG, "setVideoURI: "+mMedia.getType());
        Log.d(TAG, "DECODER: "+HWDecoderUtil.getDecoderFromDevice().toString());


        if(mMediaPlayer instanceof MediaPlayer){
            mMediaPlayer.stop();
        }

        mMediaPlayer = new MediaPlayer(sLibVLC);
        mMediaPlayer.setEventListener(mPlayerListener);




        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(this);
        vout.addCallback(this);
        vout.attachViews();

        mMediaPlayer.setMedia(mMedia);



        mMediaPlayer.play();


    }

    public void start() {
//        Log.d(TAG, "start: "+ this.toString());
    }

//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public int getDuration() {
//        return 0;
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        return 0;
//    }
//
//    @Override
//    public void seekTo(int pos) {
//
//    }
//
//    @Override
//    public boolean isPlaying() {
//        return false;
//    }
//
//    @Override
//    public int getBufferPercentage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause() {
//        return false;
//    }
//
//    @Override
//    public boolean canSeekBackward() {
//        return false;
//    }
//
//    @Override
//    public boolean canSeekForward() {
//        return false;
//    }
//
//    @Override
//    public int getAudioSessionId() {
//        return 0;
//    }
}

