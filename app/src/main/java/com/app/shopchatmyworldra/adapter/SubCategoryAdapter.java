package com.app.shopchatmyworldra.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.PRODUCTBYSUBCAT;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by legacy on 18-Aug-17.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.AutoMobileServideViewHolder> {

    private List<PRODUCTBYSUBCAT> itemList;
    private Activity context;
    int mpostion;
    categorybyId callbackcategorybyId;

    public SubCategoryAdapter(Activity context, List<PRODUCTBYSUBCAT> itemList, categorybyId callbackcategorybyId) {
        this.itemList = itemList;
        this.context = context;
        this.callbackcategorybyId = callbackcategorybyId;
    }

    @Override
    public AutoMobileServideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        AutoMobileServideViewHolder rcv = new AutoMobileServideViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(AutoMobileServideViewHolder holder, int position) {


        holder.tvItemName.setText(itemList.get(position).getProductName());
        holder.tvLoadDataByName.setText(itemList.get(position).getUserName());
        holder.ivFavroiet.setVisibility(View.GONE);
        holder.ivdFavroiet.setVisibility(View.GONE);
        holder.llcart.setVisibility(View.GONE);
        holder.tvPrice.setText("INR :"+itemList.get(position).getProductPrice());
        holder.tvParcentage.setText(itemList.get(position).getPercent()+"% off");
        Picasso.with(context)
                .load(itemList.get(position).getProductImage1())
                .into(holder.mImageView);
      /*  Glide.with(context)
                .load(itemList.get(position).getSubcatImage())
                .placeholder(R.color.gray)
                .into(holder.mImageView);*/
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class AutoMobileServideViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public View view;
        public TextView tvItemName, tvLoadDataByName,tvPrice,tvParcentage;
        public LinearLayout mLayoutItem;
        public RelativeLayout llcart;
        public ImageView ivdFavroiet;
        public ImageView ivFavroiet;
        public ImageView mImageView;



        public AutoMobileServideViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image1);
            ivdFavroiet = (ImageView) itemView.findViewById(R.id.ivdFavroiet);
            tvLoadDataByName = (TextView) itemView.findViewById(R.id.tvLoadDataByName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvParcentage = (TextView) itemView.findViewById(R.id.tvParcentage);
            ivFavroiet = (ImageView) itemView.findViewById(R.id.ivFavroiet);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            mLayoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item);
            llcart = (RelativeLayout) itemView.findViewById(R.id.llcart);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int mpostion=getAdapterPosition();
            callbackcategorybyId.getId(itemList.get(mpostion).getProductId(),itemList.get(mpostion).getUserName());
        }
    }

    public interface categorybyId
    {
        public void getId(String productId, String userName);
    }

}