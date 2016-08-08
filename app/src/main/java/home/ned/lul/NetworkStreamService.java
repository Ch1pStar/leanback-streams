package home.ned.lul;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class NetworkStreamService extends IntentService {
    private static final String TAG = "NetworkService";

    private static WebSocketServer server = null;

    public NetworkStreamService(){
        this("NetworkStreamListener");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetworkStreamService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if(server == null){
            startServer();
        }
    }

    private void startServer(){
        InetSocketAddress address = new InetSocketAddress(4444);
        server = new WebSocketServer(address) {

            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                Log.d(TAG, "Websocket connection opened from "+conn.getRemoteSocketAddress());
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Log.d(TAG, "onClose: Connection closed "+conn.getRemoteSocketAddress());
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                //TODO Add Authentication
                //Websockets currently lack a standard authentication method
//                Log.d(TAG, "onMessage: "+message);

                String vSource = null;
                String aSource = null;
                try {
                    JSONObject msg;
                    msg = new JSONObject(message);
                    vSource = msg.getString("v");
                    aSource = msg.getString("a");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(NetworkStreamService.this, NativePlaybackActivity.class);
                intent.putExtra("network-stream", vSource);
                if(aSource!=null){
                    intent.putExtra("extra-audio-stream", aSource);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                Log.e(TAG, "onError: Websocket server error", ex);
            }
        };

        server.start();
        Log.d(TAG, "Started ws server: "+ Integer.toString(server.getPort()) + server.getAddress().toString());
    }
}
