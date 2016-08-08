package home.ned.lul;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EPGService extends Service {

    public final static String TAG = "EPGService";

    private static JSONArray epgData = null;
    private static String EPG_URL = "https://api.iptv.bulsat.com/tv/stb/limit";

    public EPGService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(epgData==null || intent.hasExtra("force-update")){
            updateEPG();
        }else{
            postEPGReady();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void updateEPG() {
        new DownloadEPG().execute(EPG_URL);
    }

    public static JSONArray getEPGData(){
        return epgData;
    }

    private void postEPGReady(){
        Intent intent = new Intent("epg-loaded");
        intent.putExtra("count", epgData.length());
        LocalBroadcastManager.getInstance(EPGService.this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloadEPG extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String strFileContents = "";
            try {
                URL url = new URL(urls[0]);
                Log.d(TAG, "EPG download start: "+url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("STBDEVEL", "irensAk6Os");
                urlConnection.setRequestProperty("STB_SERIAL", "35 331659 516");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                int bytesRead = 0;
                int totalRead = 0;
                byte[] contents = new byte[2048];
                while((bytesRead = in.read(contents)) != -1) {
                    strFileContents += new String(contents, 0, bytesRead);
                    totalRead+=bytesRead;
                    publishProgress(totalRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return strFileContents;
        }

        protected void onProgressUpdate(Integer... progress) {
//            int p = progress[0];
//            Log.d(TAG, "EPG download progress: "+ Integer.toString(p));
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: "+result.length());
            try {
                epgData = new JSONArray(result);
                postEPGReady();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
