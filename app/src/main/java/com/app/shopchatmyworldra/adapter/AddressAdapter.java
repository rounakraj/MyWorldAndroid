package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.AddAdressActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.pojo.AddressResources;

import java.util.List;

/**
 * Created by MMAD on 16-08-2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.RewadsViewHolder> {

    private List<AddressResources> itemList;
    private Context context;
    int mpostion;
    addressId callbackaddressId;
    addressId1 callbackaddressId1;
    setaddressDefault callbacksetaddressDefault;
    public AddressAdapter(Context context, List<AddressResources> itemList,addressId callbackaddressId,addressId1 callbackaddressId1,setaddressDefault callbacksetaddressDefault) {
        this.itemList = itemList;
        this.context = context;
        this.callbackaddressId = callbackaddressId;
        this.callbackaddressId1 = callbackaddressId1;
        this.callbacksetaddressDefault = callbacksetaddressDefault;
    }

    @Override
    public RewadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_book_item, null);
        RewadsViewHolder rcv = new RewadsViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final RewadsViewHolder holder, final int position) {

        holder.tvAddress.setText("H.NO "+itemList.get(position).getHouseNo()+" "+ itemList.get(position).getStreet()+"\n"+itemList.get(position).getLandmark()+"\n"+itemList.get(position).getCity()+"\n"+itemList.get(position).getState()+"\n"+ itemList.get(position).getCountry()+"\n"+"Mobile: "+ itemList.get(position).getMobileNo());

        //holder.tvAddress1.setText(itemList.get(position).getAddressNo());
        if(itemList.get(position).getDefaultType().equals("1"))
        {
            holder.chkBox.setChecked(true);
        }else {
            holder.chkBox.setChecked(false);
        }

        holder.ivDeletAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    callbackaddressId.getAddressId(itemList.get(position).getAddressId(),position);
            }
        });
        holder.ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddAdressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mobileNo",itemList.get(position).getMobileNo());
                intent.putExtra("HouseNo",itemList.get(position).getHouseNo());
                intent.putExtra("Landmark",itemList.get(position).getLandmark());
                intent.putExtra("City",itemList.get(position).getCity());
                intent.putExtra("State",itemList.get(position).getState());
                intent.putExtra("Country",itemList.get(position).getCountry());
                intent.putExtra("Pincode",itemList.get(position).getPincode());
                intent.putExtra("AddressId",itemList.get(position).getAddressId());
                intent.putExtra("DefaultType",itemList.get(position).getDefaultType());
                context.startActivity(intent);

            }
        });

        if (holder.chkBox.isChecked())
        {
            callbackaddressId1.getAddressId1(itemList.get(position).getAddressId(),position);
        }
        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.chkBox.isChecked())
                {
                    callbacksetaddressDefault.getsetaddressDefault(itemList.get(position).getAddressId(),position);
                }else {
                    itemList.get(position).setDefaultType("0");
                }
            }
        });
        holder.name.setText(MyPreferences.getActiveInstance(context).getFirstName()+" "+MyPreferences.getActiveInstance(context).getLastName());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class RewadsViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public View view;
        public TextView name;
        public TextView tvAddress;
        public TextView tvAddress1;
        public ImageView ivDeletAddress;
        public ImageView ivEditAddress;
        public CheckBox chkBox;
        public RewadsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvAddress1 = (TextView) itemView.findViewById(R.id.tvAddress1);
            ivDeletAddress = (ImageView) itemView.findViewById(R.id.ivDeletAddress);
            ivEditAddress = (ImageView) itemView.findViewById(R.id.ivEditAddress);
            chkBox = (CheckBox) itemView.findViewById(R.id.chkBox);


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

        @Override
        public void onClick(View v) {
            int mposition=getAdapterPosition();
        }
    }

    public interface addressId{
        public void getAddressId(String addressId, int mposition);
    }

    public interface addressId1{
        public void getAddressId1(String addressId, int mposition);
    }


    public interface setaddressDefault{
        public void getsetaddressDefault(String addressId, int mposition);
    }

}