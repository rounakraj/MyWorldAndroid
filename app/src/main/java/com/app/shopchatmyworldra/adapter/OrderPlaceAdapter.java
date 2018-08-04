package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.OrderListPlace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by legacy on 06-Oct-17.
 */

public class OrderPlaceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<OrderListPlace> itemlist;
    private Context mContext;

    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;

    public OrderPlaceAdapter(Context mcontext, ArrayList<OrderListPlace> itemlist) {
        this.mContext = mcontext;
        this.itemlist = itemlist;

        mediacontroller = new MediaController(mcontext);
        mInflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OrderPlaceAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_rowordercomplete, null);
            holder = new OrderPlaceAdapter.ViewHolder();

            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.Spprice = (TextView) convertView.findViewById(R.id.Spprice);
            holder.paymenttype = (TextView) convertView.findViewById(R.id.paymenttype);
            holder.deliverquenty = (TextView) convertView.findViewById(R.id.deliverquenty);
            holder.image = (ImageView) convertView.findViewById(R.id.image);


            convertView.setTag(holder);

        } else {
            holder = (OrderPlaceAdapter.ViewHolder) convertView.getTag();
        }
        holder.product_name.setText(itemlist.get(position).getProductName());
        holder.price.setText("Price : " + itemlist.get(position).getProductPrice());
        holder.deliverquenty.setText("Quantity : " + itemlist.get(position).getOrderQuantity());
        holder.Spprice.setText("Quantity : " + itemlist.get(position).getProductsplPrice());
        holder.paymenttype.setText("Payment mode : " + itemlist.get(position).getPaymentType());


        if (!itemlist.get(position).getProductImage1().equals("")) {
            Picasso.with(mContext)
                    .load(itemlist.get(position).getProductImage1())
                    .into(holder.image);
        }

        return convertView;
    }

    public class ViewHolder {
        protected TextView product_name, price, Spprice, deliverquenty, paymenttype;
        protected ImageView image;

    }

}
