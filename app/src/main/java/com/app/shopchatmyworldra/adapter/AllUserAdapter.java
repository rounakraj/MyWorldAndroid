package com.app.shopchatmyworldra.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.AlluserResource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MMAD on 05-09-2017.
 */

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.CustomViewHolder> { //tvEmailId
    private ArrayList<AlluserResource> mArrayList;
    private Context mContext;
    private ProgressDialog prgDialog;
    private Invaite callbackInvite;
    private UnInvaite callbackgetUnInviteFriends;
    public AllUserAdapter(Context context, ArrayList<AlluserResource> arrayList,Invaite callbackInvite, UnInvaite callbackgetUnInviteFriends) {
        this.mArrayList = arrayList;
        this.callbackInvite = callbackInvite;
        this.callbackgetUnInviteFriends = callbackgetUnInviteFriends;
        //mFilteredList = arrayList;
        this.mContext = context;

    }

    @Override
    public AllUserAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invite_friend_item, viewGroup, false);
        AllUserAdapter.CustomViewHolder viewHolder = new AllUserAdapter.CustomViewHolder(view);

        prgDialog = new ProgressDialog(mContext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AllUserAdapter.CustomViewHolder customViewHolder, final int position) {



        if(mArrayList.get(position).getProfileImage()!=null&&!mArrayList.get(position).getProfileImage().equals("")&&mArrayList.get(position).getProfileImage().startsWith("graph"))
        {

            Picasso.with(mContext)
                    .load("https://"+mArrayList.get(position).getProfileImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(customViewHolder.imageView1);


        }else {

            if (mArrayList.get(position).getProfileImage()!=null&&!mArrayList.get(position).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(mArrayList.get(position).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(customViewHolder.imageView1);

            }
        }

        customViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        customViewHolder.tvName.setText(mArrayList.get(position).getUserName() + " " + mArrayList.get(position).getLastName());
        customViewHolder.tvStutas.setText(mArrayList.get(position).getUserStatus());
        customViewHolder.tvEmailId.setText(mArrayList.get(position).getEmailId());


        if(mArrayList.get(position).getIs_invited().equalsIgnoreCase("No")){
            customViewHolder.tvinvite.setVisibility(View.VISIBLE);
            customViewHolder.btn_unfriend.setVisibility(View.GONE);
        }else {
            customViewHolder.tvinvite.setVisibility(View.GONE);
            customViewHolder.btn_unfriend.setVisibility(View.VISIBLE);

        }
        customViewHolder.tvinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackInvite.getEmailForInviteFriends(mArrayList.get(position).getEmailId());

            }
        });
        customViewHolder.btn_unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackgetUnInviteFriends.getUnInviteFriends(mArrayList.get(position).getUserId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
 /*
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                //final String charString = charSequence.toString().toLowerCase().trim();
                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {
                    if (charString.length()==2) {
                        ArrayList<AlluserResource> filteredList = new ArrayList<>();

                        for (AlluserResource androidVersion : mArrayList) {

                            if (androidVersion.getUserName().toLowerCase().contains(charString)) {

                                filteredList.add(androidVersion);
                            }
                        }

                        mFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<AlluserResource>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView1;
        protected TextView tvStutas, tvEmailId, tvName, tvinvite, btn_unfriend;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.iv_userimge);
            this.tvName = (TextView) view.findViewById(R.id.tv_username);
            this.tvinvite = (TextView) view.findViewById(R.id.btn_invitefriendlist);
            this.tvStutas = (TextView) view.findViewById(R.id.tvStutas);
            this.tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
            this.btn_unfriend = (TextView) view.findViewById(R.id.btn_unfriend);

        }


    }

    public interface Invaite {

        public void getEmailForInviteFriends(String EmailId);
    }

    public interface UnInvaite {

        public void getUnInviteFriends(String UserId);
    }
}
