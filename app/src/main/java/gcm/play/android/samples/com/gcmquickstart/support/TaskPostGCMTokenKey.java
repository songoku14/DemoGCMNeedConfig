package gcm.play.android.samples.com.gcmquickstart.support;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by thangit14 on 8/5/16.
 */
public class TaskPostGCMTokenKey extends TaskNetworkBase<Void>{

    private String token;
    public TaskPostGCMTokenKey(Context mContext, String token) {
        super(mContext);
        this.token = token;
    }

    @Override
    protected Void genDataFromJSON(String data) throws JSONException, ParseException {
        return null;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("device_token", token);
        jsonObject.put("os", 2);
        return jsonObject;
    }

    @Override
    public String genURL() {
        return "http://52.196.211.103:3000/subscribe";
    }

    @Override
    public int genMethod() {
        return Request.Method.POST;
    }
}
