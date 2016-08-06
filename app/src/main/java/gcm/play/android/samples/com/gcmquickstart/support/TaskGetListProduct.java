package gcm.play.android.samples.com.gcmquickstart.support;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by thangit14 on 8/4/16.
 */
public class TaskGetListProduct extends TaskNetworkBase<ArrayList<ItemProduct>>{

    public TaskGetListProduct(Context mContext) {
        super(mContext);
    }

    @Override
    protected ArrayList<ItemProduct> genDataFromJSON(String data) throws JSONException, ParseException {
//        JSONObject jsonObject = new JSONObject(data);
        ArrayList<ItemProduct> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonItem = (JSONObject) jsonArray.get(i);
            ItemProduct itemProduct = new ItemProduct();
            itemProduct.setDescription(jsonItem.getString("description"));
            itemProduct.setName(jsonItem.getString("name"));
            itemProduct.setImgURL(jsonItem.getString("image"));
            itemProduct.setPrice(jsonItem.getLong("price"));
            list.add(itemProduct);
        }
        return list;
    }

    @Override
    public JSONObject genBodyParam() throws JSONException {
        return null;
    }

    @Override
    public String genURL() {
        return "http://52.196.211.103:3000/products";
    }

    @Override
    public int genMethod() {
        return Request.Method.GET;
    }
}
