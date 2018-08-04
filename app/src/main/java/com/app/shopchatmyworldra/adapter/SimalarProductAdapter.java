package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.ProductDetailActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.SimilarList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by legacy on 05-Sep-17.
 */

public class SimalarProductAdapter extends RecyclerView.Adapter<SimalarProductAdapter.CustomViewHolder> {
    private ArrayList<SimilarList> followers;
    private Context mContext;
    public SimalarProductAdapter(Context context, ArrayList<SimilarList> followers) {
        this.followers = followers;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.similar_product,viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final SimilarList follower = followers.get(i);
        //customViewHolder.tvPrcent.setText(follower.getProductOffer());
        customViewHolder.tvPrice.setText("INR :"+follower.getProductPrice());
        customViewHolder.tvItemName.setText(follower.getProductName());

        if(!follower.getProductImage1().equals(""))
        Picasso.with(mContext)
                .load(follower.getProductImage1())
                .into(customViewHolder.image1);

        customViewHolder.setClickListener(new SimalarProductAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent in = new Intent(mContext, ProductDetailActivity.class);
                    in.putExtra("productId",followers.get(position).getProductId());
                    in.putExtra("userName",followers.get(position).getUserName());
                    mContext.startActivity(in);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return (null != followers ? followers.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView image1;
        protected TextView tvItemName,tvPrice,tvPrcent;
        private SimalarProductAdapter.ItemClickListener clickListener;
        public CustomViewHolder(View view) {
            super(view);
            this.image1 = (ImageView) view.findViewById(R.id.image1);
            this.tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            this.tvPrice=(TextView) view.findViewById(R.id.tvPrice);
            this.tvPrcent=(TextView)view.findViewById(R.id.tvPrcent);
            view.setOnClickListener(this);
        }

        public void setClickListener(SimalarProductAdapter.ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
    }
    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }

}

