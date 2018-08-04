package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shopchatmyworldra.ActivitySubCategory;
import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.CategoryAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.CategoryResource;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BrowseCategoryFragment extends Fragment implements CategoryAdapter.sortbydata {
    private View view;
    RecyclerView recyclerView;
    private ArrayList<CategoryResource> categoryResources;
    private CategoryAdapter mAdapter;
    private Activity mcontext;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mcontext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_category, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);


        GridLayoutManager llm = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //getServiceData();
        categoryResources = new ArrayList<>();
        mAdapter = new CategoryAdapter(getActivity(), categoryResources, BrowseCategoryFragment.this);
        recyclerView.setAdapter(mAdapter);
        if (CommanMethod.isOnline(mcontext)) {
            validateGETCATGORY();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }

        return view;
    }

    @Override
    public void getIddata(String catId) {

        Intent intent = new Intent(mcontext, ActivitySubCategory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("catId",catId);
        startActivity(intent);
        mcontext.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    private void validateGETCATGORY() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(WebServices.GETCATEGORY, params,
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
                        parseResult(responseCode.toString());

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


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                JSONArray jsonArray = response.getJSONArray("categorylist");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    CategoryResource resource = new CategoryResource();
                    resource.setCatId(object.getString("catId"));
                    resource.setCatName(object.getString("catName"));
                    resource.setCatImage(object.getString("catImage"));
                    categoryResources.add(resource);
                }
                if (categoryResources != null) {
                    mAdapter.notifyDataSetChanged();

                }

            } else {
                CommanMethod.showAlert(message, mcontext);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
