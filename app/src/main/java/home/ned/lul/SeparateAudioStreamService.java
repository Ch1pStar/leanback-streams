package home.ned.lul;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;


public class SeparateAudioStreamService extends Service implements MediaPlayer.OnPreparedListener {
    public final static String TAG = "ExtraAudioStreamService";
    private static final String ACTION_PLAY = "EXTRA_AUDIO_STREAM_PLAY";
    MediaPlayer mMediaPlayer = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//            if (intent.getAction().equals(ACTION_PLAY)) {
        mMediaPlayer = new MediaPlayer();
        String url = intent.getSerializableExtra("extra-audio-stream").toString();
        try {
            mMediaPlayer.setDataSource(url);
        }catch (IOException e){
            Log.e(TAG, "Extra audio stream IO error "+url, e);
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync(); // prepare async to not block main thread
//            }
        return 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}