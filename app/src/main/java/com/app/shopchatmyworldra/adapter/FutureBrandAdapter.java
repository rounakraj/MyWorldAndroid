package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.FuturebrandList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by legacy on 07-Nov-17.
 */

public class FutureBrandAdapter extends RecyclerView.Adapter<FutureBrandAdapter.ViewHolder> {

    ArrayList<FuturebrandList> mfuturebrandList;
    Context context;

    public FutureBrandAdapter(Context context,  ArrayList<FuturebrandList> mfuturebrandList) {
        super();
        this.context = context;
        this.mfuturebrandList = mfuturebrandList;
    }

    @Override
    public FutureBrandAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listrow_futurebrand, viewGroup, false);
        FutureBrandAdapter.ViewHolder viewHolder = new FutureBrandAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FutureBrandAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_brandName.setText(mfuturebrandList.get(i).getBrandName());
        if(!mfuturebrandList.get(i).getBrandImage().equals(""))
        {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(mfuturebrandList.get(i).getBrandImage())
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


        viewHolder.setClickListener(new FutureBrandAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mfuturebrandList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView tv_brandName;
        private FutureBrandAdapter.ItemClickListener clickListener;
        private ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tv_brandName = (TextView) itemView.findViewById(R.id.tv_brandName);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(FutureBrandAdapter.ItemClickListener itemClickListener) {
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