package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.CategoryResource;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MMAD on 10-07-2017.
 */

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryResource> itemList;
    private Context context;
    sortbydata callbacksortbydata;
    public CategoryAdapter(Context context, List<CategoryResource> itemList,sortbydata callbacksortbydata) {
        this.itemList = itemList;
        this.callbacksortbydata = callbacksortbydata;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, null);
        CategoryViewHolder rcv = new CategoryViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

            holder.categoryname.setText(itemList.get(position).getCatName());
           // holder.ivCategory.setImageResource(imageId[position]);

        if(!itemList.get(position).getCatImage().equals(""))
        Picasso.with(context)
                .load(itemList.get(position).getCatImage())
                .into( holder.ivCategory);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder  implements RecyclerView.OnClickListener{

        public View view;
        public TextView categoryname;
        public ImageView ivCategory;




        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryname = (TextView) itemView.findViewById(R.id.category_name);
            ivCategory = (ImageView) itemView.findViewById(R.id.image1);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int mposition=getAdapterPosition();
            callbacksortbydata.getIddata(itemList.get(mposition).getCatId());

        }
    }

    public interface sortbydata
    {
        public void getIddata(String catId);
    }

}