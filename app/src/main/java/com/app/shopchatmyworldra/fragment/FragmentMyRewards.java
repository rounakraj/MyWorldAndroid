package com.app.shopchatmyworldra.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.TermsAndConditionActivity;
import com.app.shopchatmyworldra.adapter.MyRewardsAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.MyRewardsResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMyRewards extends Fragment {
    private View view;
    private LinearLayout llInviteFriends;
    private LinearLayout llMyRewards;
    private TextView terms;
    private TextView tvInrBalance;
    private TextView tvTotalPerson;
    private CircleImageView ivImageView;
    RecyclerView recyclerView;
    private ArrayList<MyRewardsResources> myRewardsResources;
    private MyRewardsAdapter mAdapter;
    private Context mcontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_rewards, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_rewards);
        llInviteFriends = (LinearLayout) view.findViewById(R.id.llInviteFriends);
        llMyRewards = (LinearLayout) view.findViewById(R.id.llMyRewards);
        terms = (TextView) view.findViewById(R.id.terms);
        tvInrBalance = (TextView) view.findViewById(R.id.tvInrBalance);
        tvTotalPerson = (TextView) view.findViewById(R.id.tvTotalPerson);
        ivImageView = (CircleImageView) view.findViewById(R.id.ivImageView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);
        if (CommanMethod.isOnline(mcontext)) {
            getMyrefer();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, TermsAndConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        llInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InviteFriendFragment fragmentMyRewards = new InviteFriendFragment();
                start_fragment(fragmentMyRewards);
            }
        });
        llMyRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(mcontext)) {
                    FragmentMyRewards fragmentMyRewards = new FragmentMyRewards();
                    start_fragment(fragmentMyRewards);
                } else {
                    View sbView = MainActivity.snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
                    MainActivity.snackbar.show();
                }


            }
        });
        try {
            if(!MyPreferences.getActiveInstance(mcontext).getprofileimg().equals(""))
            {
                Picasso.with(mcontext)
                        .load(MyPreferences.getActiveInstance(mcontext).getprofileimg())
                        .into(ivImageView);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return view;
    }

    private void start_fragment(Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, frag);
        fragmentTransaction.commit();
    }


    private void getMyrefer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.getMyrefer, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(getActivity(),"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultRefer(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }


    private void parseResultRefer(String result) {

        try {
            JSONObject response = new JSONObject(result);
            Log.e("inviteFriendFragment","resoponse"+response.toString(2));
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                tvInrBalance.setText(response.getString("earn"));
                tvTotalPerson.setText("Total "+response.getString("totalRefer"));
                JSONArray jsonArray=response.getJSONArray("referList");
                myRewardsResources = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object=jsonArray.getJSONObject(i);
                    MyRewardsResources item = new MyRewardsResources();
                    item.setFirstName(object.getString("firstName"));
                    item.setLastname(object.getString("firstName"));
                    item.setEarn(response.getString("earn"));
                    myRewardsResources.add(item);
                }
                mAdapter = new MyRewardsAdapter(getActivity(), myRewardsResources);
                recyclerView.setAdapter(mAdapter);




            } else {

                CommanMethod.showAlert(message, mcontext);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
