package gcm.play.android.samples.com.gcmquickstart.support;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thangit14 on 8/4/16.
 */
public abstract class TaskNetworkBase<T extends Object> {
    private static final int NETWORK_TIME_OUT_DEFAULT = 30000;

    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private Request<T> mRequest;
    private Context mContext;
    private RequestQueue mRequestQueue;
//    private String session;
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;

    protected TaskNetworkBase(Context mContext) {
        this.mContext = mContext;
//        sharedPreferences = mContext.getSharedPreferences(Constant.ABMS_SHARED_PREFRENCES, Context.MODE_PRIVATE);
//        session = sharedPreferences.getString(Constant.ACCESS_TOKEN, "");
    }

    public final void request(final Response.Listener<T> listener, final ErrorListener errorListener) {
        mRequest = new Request<T>(genMethod(), genURL(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                /**
                 * if volleyError is a instance of VolleyErrorHelper ->error from sever
                 * else create new VolleyErrorHelper to handle error from Volley
                 */
                VolleyErrorHelper volleyErrorHelper;
                if (volleyError instanceof VolleyErrorHelper) {
                    volleyErrorHelper = (VolleyErrorHelper) volleyError;
                } else {
                    volleyErrorHelper = new VolleyErrorHelper(volleyError);
                }
                errorListener.onErrorListener(volleyErrorHelper.getErrorCode(), volleyErrorHelper.getMessageError());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
//                headers.put("session", session);
                headers.put("os", "2");

                headers.put("udid", "none");
                headers.put("agent", "none");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = null;
                try {
                    jsonObject = genBodyParam();
                } catch (JSONException e) {
                    e.printStackTrace();
                    VolleyErrorHelper volleyErrorHelper = new VolleyErrorHelper(e.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
                    Response.error(volleyErrorHelper);
                }
                Log.e("REQUEST -->", genURL());

                if (null == jsonObject) {
                    Log.e("REQUEST -->", "request with no parameter");
                    return "".getBytes();
                } else {
                    Log.e("REQUEST --> ", "json = " + jsonObject.toString());
                    byte[] json = jsonObject.toString().getBytes();
                    return json;
                }
            }


            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
                String data = new String(networkResponse.data);
                Log.e("TaskNetworkBase --> parseNetworkResponse", "ServerResponse: " + data);

                T result = null;
                VolleyErrorHelper volleyErrorHelper;
                try {
//                    volleyErrorHelper = validData(data);
//                    if (null == volleyErrorHelper) {
                        result = genDataFromJSON(data);
                        return Response.success(result, getCacheEntry());
//                    } else {
//                        return Response.error(volleyErrorHelper);
//                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    volleyErrorHelper = new VolleyErrorHelper(" Error code parser json ", ExceptionConstant.ERROR_CODE_PARSE_JSON);
                    return Response.error(volleyErrorHelper);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    volleyErrorHelper = new VolleyErrorHelper(" Error code parser json ", ExceptionConstant.ERROR_CODE_PARSE_JSON);
                    return Response.error(volleyErrorHelper);
                }
            }

            @Override
            protected void deliverResponse(T data) {
                listener.onResponse(data);
            }
        };

        mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();
        mRequest.setRetryPolicy(new DefaultRetryPolicy(NETWORK_TIME_OUT_DEFAULT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mRequest);
    }

    protected abstract T genDataFromJSON(String data) throws JSONException, ParseException;

    private VolleyErrorHelper validData(String json) {
        VolleyErrorHelper volleyErrorHelper = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt(CODE);
            if (code != 0) {
                String message = jsonObject.getString(MESSAGE);
                volleyErrorHelper = new VolleyErrorHelper(message, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            volleyErrorHelper = new VolleyErrorHelper(e.getMessage(), ExceptionConstant.ERROR_CODE_PARSE_JSON);
        }
        return volleyErrorHelper;
    }



    public abstract JSONObject genBodyParam() throws JSONException;

    public abstract String genURL();

    public abstract int genMethod();

    public void cancelRequest(){
        if (null != mRequest){
            mRequest.cancel();
        }
    }

    public interface ErrorListener {
        void onErrorListener(int errorCode, String errorMessage);
    }
}
