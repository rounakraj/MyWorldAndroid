package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.ProductDetailActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.Nearby;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by legacy on 16-Aug-17.
 */

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    ArrayList<Nearby> mNearby;
    Context context;

    public NearbyAdapter(Context context,  ArrayList<Nearby> mNearby) {
        super();
        this.context = context;
        this.mNearby = mNearby;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_itemnearby, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(mNearby.get(i).getItemName());

        if(!mNearby.get(i).getItemImg().equals(""))
        {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(mNearby.get(i).getItemImg())
                    .error(R.drawable.images_not_found)
                    .placeholder(R.color.gray)
                    .resize(320, 230)
                    .into(viewHolder.imgThumbnail, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            if (viewHolder.progressBar != null) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (viewHolder.progressBar != null) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


           /* Picasso.with(context)
                    .load(mNearby.get(i).getItemImg())
                    .error(R.drawable.images_not_found)
                    .placeholder(R.color.gray)
                    .resize(400,400).centerCrop()
                    .into(viewHolder.imgThumbnail);*/
        }


        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {
                    Intent in = new Intent(context, ProductDetailActivity.class);
                    in.putExtra("productId",mNearby.get(position).getItemId());
                    in.putExtra("userName",mNearby.get(position).getUserName());
                    context.startActivity(in);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearby.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView tvSpecies;
        private ItemClickListener clickListener;
        private ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progressBar);
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
}