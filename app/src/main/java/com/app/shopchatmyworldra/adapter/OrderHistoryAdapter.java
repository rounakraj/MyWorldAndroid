package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.OrderHistoryResources;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MMAD on 16-08-2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.RewadsViewHolder> {

    private List<OrderHistoryResources> itemList;
    private Context context;
    int mpostion;

    public OrderHistoryAdapter(Context context, List<OrderHistoryResources> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RewadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, null);
        RewadsViewHolder rcv = new RewadsViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RewadsViewHolder holder, int position) {

        holder.product_name.setText(itemList.get(position).getProductName());
        holder.deliveridon.setText(itemList.get(position).getDeliveron());
        holder.tvAddress.setText(itemList.get(position).getDeliveron());
        holder.tvAddress.setText("H.NO "+itemList.get(position).getHouseNo()+" "+ itemList.get(position).getStreet()+"\n"+itemList.get(position).getLandmark()+"\n"+itemList.get(position).getCity()+"\n"+itemList.get(position).getState()+"\n"+ itemList.get(position).getCountry());
        holder.paymenttype.setText(itemList.get(position).getPaymentType());
        holder.deliverquenty.setText("Qty: "+itemList.get(position).getOrderQuantity());
        holder.price.setText("Price: "+itemList.get(position).getProductPrice());
        holder.Spprice.setText("Special Price: "+itemList.get(position).getProductsplPrice());
        if(!itemList.get(position).getProductImage1().equals(""))
        {
            Picasso.with(context)
                    .load(itemList.get(position).getProductImage1())
                    .into(holder.image);
        }

        holder.ivArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivArrowDown.setVisibility(View.GONE);
                holder.ivArrowUp.setVisibility(View.VISIBLE);
                holder.llBoxDeliverStustes.setVisibility(View.VISIBLE);
            }
        });

        holder.ivArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivArrowUp.setVisibility(View.GONE);
                holder.ivArrowDown.setVisibility(View.VISIBLE);
                holder.llBoxDeliverStustes.setVisibility(View.GONE);
            }
        });
        String orderStatus=itemList.get(position).getOrderStatus();
        if(orderStatus.equals("0"))
        {
            holder.ivPending.setImageResource(R.mipmap.green1);
            holder.tvDatePending.setText(itemList.get(position).getDeliveron());

        }else if(orderStatus.equals("1"))
        {
            holder.ivPending.setImageResource(R.mipmap.blueimage);
            holder.ivProcessing.setImageResource(R.mipmap.green1);

            holder.tvDatePending.setText(itemList.get(position).getDeliveron());
            holder.tvDateProcessing.setText(itemList.get(position).getProcessingDate());

            holder.lineProcessing.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));



        }else if(orderStatus.equals("2"))
        {
            holder.ivDispatch.setImageResource(R.mipmap.green1);
            holder.ivPending.setImageResource(R.mipmap.blueimage);
            holder.ivProcessing.setImageResource(R.mipmap.blueimage);

            holder.tvDatePending.setText(itemList.get(position).getDeliveron());
            holder.tvDateProcessing.setText(itemList.get(position).getProcessingDate());
            holder.tvDateDispatch.setText(itemList.get(position).getDispatchDate());


            holder.lineProcessing.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineDispatch.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));



        }else if(orderStatus.equals("3"))
        {
            holder.ivShipping.setImageResource(R.mipmap.green1);
            holder.ivDispatch.setImageResource(R.mipmap.blueimage);
            holder.ivPending.setImageResource(R.mipmap.blueimage);
            holder.ivProcessing.setImageResource(R.mipmap.blueimage);


            holder.tvDatePending.setText(itemList.get(position).getDeliveron());
            holder.tvDateProcessing.setText(itemList.get(position).getProcessingDate());
            holder.tvDateDispatch.setText(itemList.get(position).getDispatchDate());
            holder.tvDateShipping.setText(itemList.get(position).getShippingDate());

            holder.lineProcessing.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineDispatch.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineShipping.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));


        }else if(orderStatus.equals("4"))
        {
            holder.ivDeliverd.setImageResource(R.mipmap.blueimage);
            holder.ivShipping.setImageResource(R.mipmap.blueimage);
            holder.ivDispatch.setImageResource(R.mipmap.blueimage);
            holder.ivPending.setImageResource(R.mipmap.blueimage);
            holder.ivProcessing.setImageResource(R.mipmap.blueimage);



            holder.tvDatePending.setText(itemList.get(position).getDeliveron());
            holder.tvDateProcessing.setText(itemList.get(position).getProcessingDate());
            holder.tvDateDispatch.setText(itemList.get(position).getDispatchDate());
            holder.tvDateShipping.setText(itemList.get(position).getShippingDate());
            holder.tvDateDeliverd.setText(itemList.get(position).getDeliveredDate());


            holder.lineProcessing.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineDispatch.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineShipping.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.lineDeliverd.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

        }


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class RewadsViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView product_name, deliveridon, paymenttype,deliverquenty,price,Spprice;
        public LinearLayout llBoxDeliverStustes;
        public ImageView image;
        public ImageView ivArrowDown;
        public ImageView ivArrowUp;
        public ImageView ivPending;
        public View lineProcessing;
        public ImageView ivProcessing;
        public View lineDispatch;
        public ImageView ivDispatch;
        public View lineShipping;
        public ImageView ivShipping;
        public View lineDeliverd;
        public ImageView ivDeliverd;
        public TextView tvPending;
        public TextView tvProcessing;
        public TextView tvDispatch;
        public TextView tvShipping;
        public TextView tvDeliverd;
        public TextView tvDatePending;
        public TextView tvDateProcessing;
        public TextView tvDateDispatch;
        public TextView tvDateShipping;
        public TextView tvDateDeliverd;
        public TextView tvAddress;


        public RewadsViewHolder(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            deliveridon = (TextView) itemView.findViewById(R.id.deliveridon);
            paymenttype = (TextView) itemView.findViewById(R.id.paymenttype);
            deliverquenty = (TextView) itemView.findViewById(R.id.deliverquenty);
            price = (TextView) itemView.findViewById(R.id.price);
            Spprice = (TextView) itemView.findViewById(R.id.Spprice);
            image = (ImageView) itemView.findViewById(R.id.image);
            llBoxDeliverStustes = (LinearLayout) itemView.findViewById(R.id.llBoxDeliverStustes);

            ivPending=(ImageView)itemView.findViewById(R.id.ivPending);
            ivProcessing=(ImageView)itemView.findViewById(R.id.ivProcessing);
            ivDispatch=(ImageView)itemView.findViewById(R.id.ivDispatch);
            ivShipping=(ImageView)itemView.findViewById(R.id.ivShipping);
            ivDeliverd=(ImageView)itemView.findViewById(R.id.ivDeliverd);

            lineProcessing=(View)itemView.findViewById(R.id.lineProcessing);
            lineDispatch=(View)itemView.findViewById(R.id.lineDispatch);
            lineShipping=(View)itemView.findViewById(R.id.lineShipping);
            lineDeliverd=(View)itemView.findViewById(R.id.lineDeliverd);
            tvPending=(TextView)itemView.findViewById(R.id.tvPending);
            tvProcessing=(TextView)itemView.findViewById(R.id.tvProcessing);
            tvDispatch=(TextView)itemView.findViewById(R.id.tvDispatch);
            tvShipping=(TextView)itemView.findViewById(R.id.tvShipping);
            tvDeliverd=(TextView)itemView.findViewById(R.id.tvDeliverd);
            tvDatePending=(TextView)itemView.findViewById(R.id.tvDatePending);
            tvDateProcessing=(TextView)itemView.findViewById(R.id.tvDateProcessing);
            tvDateDispatch=(TextView)itemView.findViewById(R.id.tvDateDispatch);
            tvDateShipping=(TextView)itemView.findViewById(R.id.tvDateShipping);
            tvDateDeliverd=(TextView)itemView.findViewById(R.id.tvDateDeliverd);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);

            ivArrowDown=(ImageView)itemView.findViewById(R.id.ivArrowDown);
            ivArrowUp=(ImageView)itemView.findViewById(R.id.ivArrowUp);


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