package com.app.shopchatmyworldra.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.CartListResourse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by legacy on 18-Aug-17.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.RewadsViewHolder> {

    private List<CartListResourse> itemList;
    private Context context;
    String compareValue;
    int mpostion;
    private ProgressDialog prgDialog;
    removeCart callbackremoveCart;
    movetowishlist callbackMovetowishlist;
    addQuanty callbackaddQuanty;
    private String quantity="0";
    int spinnerPosition;
    String[] bankNames={"Quantity","1","2","3","4","5","6","7","8","9","10"};
    public CartListAdapter(Context context, List<CartListResourse> itemList,removeCart callbackremoveCart,movetowishlist callbackMovetowishlist, addQuanty callbackaddQuanty) {
        this.itemList = itemList;
        this.context = context;
        this.callbackremoveCart = callbackremoveCart;
        this.callbackMovetowishlist = callbackMovetowishlist;
        this.callbackaddQuanty = callbackaddQuanty;
    }

    @Override
    public RewadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_itemcart, null);
        RewadsViewHolder rcv = new RewadsViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RewadsViewHolder holder, final int position) {

        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        holder.name.setText(itemList.get(position).getProductName());
        holder.tvPrice.setText("Price :"+itemList.get(position).getSinglePrice());
        holder.tvSpecialPrice.setText("Special price :"+itemList.get(position).getSinglesplPrice());
        //.tvQty.setText("Qty :"+itemList.get(position).getQuantity());
        compareValue=itemList.get(position).getQuantity();
      // holder.spQuanty.setIt(Integer.parseInt(itemList.get(position).getQuantity()));


        ArrayAdapter spin_subcategoryarray = new ArrayAdapter(context, android.R.layout.simple_spinner_item, bankNames);
        spin_subcategoryarray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spQuanty.setAdapter(spin_subcategoryarray);

        if (!compareValue.equals(null)) {
            spinnerPosition = spin_subcategoryarray.getPosition(compareValue);
           holder.spQuanty.setSelection(spinnerPosition);
        }
        holder.spQuanty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                quantity=parent.getItemAtPosition(pos).toString();
                itemList.get(position).setQuantity(quantity);
                callbackaddQuanty.getCartId(itemList.get(position).getCartId(),quantity,position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Picasso.with(context)
                .load(itemList.get(position).getProductImage1())
                .into( holder.ivItemimage);

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callbackremoveCart.getcartId(itemList.get(position).getCartId(),position,itemList.get(position).getProductPrice());
            }
        });

        holder.btnmovetowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              callbackMovetowishlist.getmovewishlist(itemList.get(position).getProductId(),itemList.get(position).getUserName(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class RewadsViewHolder extends RecyclerView.ViewHolder  {

        public View view;
        public TextView name;
        public TextView tvPrice;
        public TextView tvSpecialPrice;
        public TextView tvQty;
        public ImageView ivItemimage;
        Spinner spQuanty;
        public TextView btnRemove,btnmovetowishlist;
        public RewadsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvTitle);
           // tvQty = (TextView) itemView.findViewById(R.id.tvQty);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvSpecialPrice = (TextView) itemView.findViewById(R.id.tvSpecialPrice);
            ivItemimage = (ImageView) itemView.findViewById(R.id.ivItemimage);
            spQuanty = (Spinner) itemView.findViewById(R.id.spQuanty);

            btnRemove = (TextView) itemView.findViewById(R.id.btnRemove);
            btnmovetowishlist = (TextView) itemView.findViewById(R.id.btnmovetowishlist);
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

    public interface removeCart
    {
        public void getcartId(String cartId, int positon, String productprice);
    }
    public interface movetowishlist{

        public void getmovewishlist(String productid, String username,int position);
    }

    public interface addQuanty{

        public void getCartId(String cartId, String quantity,int position);
    }


}
