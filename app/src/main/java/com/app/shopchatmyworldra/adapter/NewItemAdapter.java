package com.app.shopchatmyworldra.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.ProductDetailActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.NewItemResources;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MMAD on 29-08-2017.
 */

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.ItemServideViewHolder> {

    private List<NewItemResources> itemList;
    private Activity context;
    int mpostion;

    public NewItemAdapter(Activity context, List<NewItemResources> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ItemServideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.newitem, null);
        ItemServideViewHolder rcv = new ItemServideViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemServideViewHolder holder, int position) {


       holder.tvItemName.setText(itemList.get(position).getName());
        holder.price.setText("INR :"+itemList.get(position).getPrice());
        holder.usename.setText(itemList.get(position).getUsername());
        holder.tvParcentage.setText(itemList.get(position).getPercent()+"% off");
        Picasso.with(context)
                .load(itemList.get(position).getImage())
                .into(holder.mImageView);



       holder.setClickListener(new ItemClickListener() {
           @Override
           public void onClick(View view, int position, boolean isLongClick) {
               if (isLongClick) {

               } else {
                   Intent in = new Intent(context, ProductDetailActivity.class);
                   in.putExtra("productId",itemList.get(position).getProductid());
                   in.putExtra("userName",itemList.get(position).getUsername());
                   context.startActivity(in);
                   context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class ItemServideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        private NewItemAdapter.ItemClickListener clickListener;
        public View view;
        public TextView tvItemName, price,usename,tvParcentage;
        public LinearLayout mLayoutItem;
        public ImageView mImageView;



        public ItemServideViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image1);
            mLayoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item);
            tvItemName=(TextView)itemView.findViewById(R.id.tvItemName);
            price=(TextView)itemView.findViewById(R.id.price);
            tvParcentage=(TextView)itemView.findViewById(R.id.tvParcentage);
            usename=(TextView)itemView.findViewById(R.id.usename);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }


        public void setClickListener(NewItemAdapter.ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }

}