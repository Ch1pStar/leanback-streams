package home.ned.lul;


import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

//import javax.websocket.server.ServerEndpoint;

public class EditTextPresenter extends Presenter {
    private static final String TAG = "EditTextPresenter";
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        EditText et = (EditText) parent.findViewById(R.id.edit_message);
        return new ViewHolder(et);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
