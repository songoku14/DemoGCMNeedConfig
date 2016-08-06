package gcm.play.android.samples.com.gcmquickstart.support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gcm.play.android.samples.com.gcmquickstart.R;

/**
 * Created by thangit14 on 8/4/16.
 */
public class AdapterListProduct extends ArrayAdapter<ItemProduct>{
    private Context context;
    private LayoutInflater layoutInflater;
    public AdapterListProduct(Context context, ArrayList<ItemProduct> datas) {
        super(context, -1, datas);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemProduct itemProduct = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_product, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtDexcription = (TextView) convertView.findViewById(R.id.txt_description);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
            viewHolder.imgPhoto = (ImageView) convertView.findViewById(R.id.img_photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtPrice.setText(itemProduct.getPrice() + "");
        viewHolder.txtDexcription.setText(itemProduct.getName());
        Glide.with(getContext())
                .load(itemProduct.getImgURL())
//                .placeholder(R.drawable.avatar_default)
//                .crossFade()
                .into(viewHolder.imgPhoto);
        
        return convertView;
    }

    private class ViewHolder{
        TextView txtDexcription;
        TextView txtPrice;
        ImageView imgPhoto;
    }
}
