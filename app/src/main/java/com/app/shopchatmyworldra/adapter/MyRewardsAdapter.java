package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.MyRewardsResources;

import java.util.List;

/**
 * Created by MMAD on 10-07-2017.
 */

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.RewadsViewHolder> {

    private List<MyRewardsResources> itemList;
    private Context context;
    int mpostion;

    public MyRewardsAdapter(Context context, List<MyRewardsResources> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RewadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrewards_item, null);
        RewadsViewHolder rcv = new RewadsViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RewadsViewHolder holder, int position) {

        holder.reward_name.setText(itemList.get(position).getFirstName()+" "+itemList.get(position).getLastname());
        holder.tvEanr.setText(itemList.get(position).getEarn());


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class RewadsViewHolder extends RecyclerView.ViewHolder  {

        public View view;
        public TextView reward_name;
        public TextView tvEanr;




        public RewadsViewHolder(View itemView) {
            super(itemView);
            reward_name = (TextView) itemView.findViewById(R.id.reward_name);
            tvEanr = (TextView) itemView.findViewById(R.id.tvEanr);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mpostion = getAdapterPosition();
                    Intent paymentWithService = new Intent(context, ServiceDetailActivity.class);
                    paymentWithService.putExtra("productId",itemList.get(mpostion).getProductid());
                    context.startActivity(paymentWithService);
                }
            });*/

        }
    }

}