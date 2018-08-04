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

import com.app.shopchatmyworldra.ActivitySortBy;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.SubCategoryResources;

import java.util.ArrayList;

/**
 * Created by legacy on 02-Sep-17.
 */

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ItemServideViewHolder> {
    ArrayList<SubCategoryResources> arrayList;
    private Activity context;
    int mpostion;

    public SubAdapter(Activity context,  ArrayList<SubCategoryResources> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ItemServideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listrow_subitem, null);
        ItemServideViewHolder rcv = new ItemServideViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemServideViewHolder holder, int position) {
        holder.tvItemName.setText(arrayList.get(position).getSubcatName());

    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    class ItemServideViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View view;
        public TextView tvItemName, price,usename;
        public LinearLayout mLayoutItem;
        public ImageView mImageView;


        public ItemServideViewHolder(View itemView) {
            super(itemView);
            mLayoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item);
            tvItemName=(TextView)itemView.findViewById(R.id.tvSubcat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mpositon=getAdapterPosition();
            Intent intent=new Intent(context,ActivitySortBy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("subcatId",arrayList.get(mpositon).getSubcatId());
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

}
