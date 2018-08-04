package com.app.shopchatmyworldra.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.shopchatmyworldra.ProductDetailActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.WishListResources;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MMAD on 09-07-2017.
 */

public class WishListRecyclerViewAdapter extends RecyclerView.Adapter<WishListRecyclerViewAdapter.AutoMobileServideViewHolder> {

    private List<WishListResources> itemList;
    private Activity context;
    int mpostion;
    dislikecall callbackdislikecall;
    movetocart  callbackmovetocart;
    private String quantity="0";
    String[] bankNames={"Quantity","1","2","3","4","5","6","7","8","9","10"};
    public WishListRecyclerViewAdapter(Activity context, List<WishListResources> itemList,dislikecall callbackdislikecall,movetocart  callbackmovetocart) {
        this.itemList = itemList;
        this.context = context;
        this.callbackdislikecall=callbackdislikecall;
        this.callbackmovetocart=callbackmovetocart;
    }

    @Override
    public AutoMobileServideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        AutoMobileServideViewHolder rcv = new AutoMobileServideViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final AutoMobileServideViewHolder holder, final int position) {
        holder.tvItemName.setText(itemList.get(position).getProductName());
        holder.tvLoadDataByName.setText(itemList.get(position).getUserName());
        holder.tvPrice.setText("INR :"+itemList.get(position).getProductPrice());
        holder.tvParcentage.setText(itemList.get(position).getPercent()+"% off");
        Picasso.with(context)
                .load(itemList.get(position).getProductImage1())
                .into(holder.mImageView);

        holder.ivFavroiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivFavroiet.setImageResource(R.drawable.favorite);
                callbackdislikecall.getIdNamedislike(itemList.get(position).getProductId(),itemList.get(position).getUserName(),position);

            }
        });

        ArrayAdapter spin_subcategoryarray = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bankNames);
        spin_subcategoryarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spQuanty.setAdapter(spin_subcategoryarray);

        holder.spQuanty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos>0)
                {
                    quantity=parent.getItemAtPosition(pos).toString();


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.ivMovetocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackmovetocart.movecart(itemList.get(position).getProductId(),quantity);
            }
        });



        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent in = new Intent(context, ProductDetailActivity.class);
                    in.putExtra("productId",itemList.get(position).getProductId());
                    in.putExtra("userName",itemList.get(position).getUserName());
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

    class AutoMobileServideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        private ItemClickListener clickListener;
        public View view;
        public TextView tvItemName;
        public TextView tvPrice;
        public TextView tvParcentage;
        public ImageView ivMovetocart;
        public TextView tvLoadDataByName;
        public Spinner spQuanty;

        public LinearLayout mLayoutItem;
        public ImageView mImageView;
        public ImageView ivdFavroiet;
        public ImageView ivFavroiet;



        public AutoMobileServideViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image1);
            ivdFavroiet = (ImageView) itemView.findViewById(R.id.ivdFavroiet);
            ivFavroiet = (ImageView) itemView.findViewById(R.id.ivFavroiet);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvParcentage = (TextView) itemView.findViewById(R.id.tvParcentage);
            spQuanty = (Spinner) itemView.findViewById(R.id.spQuanty);
            ivMovetocart = (ImageView) itemView.findViewById(R.id.ivMovetocart);
            tvLoadDataByName = (TextView) itemView.findViewById(R.id.tvLoadDataByName);

            mLayoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        public void setClickListener(ItemClickListener itemClickListener) {
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




    public interface dislikecall{
        public void getIdNamedislike(String productId, String Name, int Position);
    }

    public interface movetocart{
        public void movecart(String productId, String quantity);
    }






}