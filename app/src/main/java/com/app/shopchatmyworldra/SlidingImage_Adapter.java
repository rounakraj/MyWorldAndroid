package com.app.shopchatmyworldra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONU on 29/08/15.
 */
public class SlidingImage_Adapter  extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private Context mcontext;
    private ImageView imageIcon;
    private RelativeLayout rlParent;
    public ProgressBar progressBar;

    @SuppressLint("NewApi")
    @Override
    public void finishUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.finishUpdate(container);

    }

    public SlidingImage_Adapter(Context context, ArrayList<String> IMAGES) {

        super();
        this.IMAGES = IMAGES;
        this.mcontext = context;

    }

    @Override
    public int getCount() {

        return IMAGES.size();

    }

    @Override
    public boolean isViewFromObject(View collection, Object object) {

        return collection == ((View) object);
    }

    @Override
    public Object instantiateItem(View collection, int position) {

        // Inflating layout
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.slidingimages_layout, null);
        imageIcon = (ImageView) view.findViewById(R.id.image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        try {

            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(mcontext)
                    .load(IMAGES.get(position))
                    .error(R.drawable.images_not_found)
                    .placeholder(R.color.gray)
                    .into(imageIcon, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((ViewPager) collection).addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}





















/*extends PagerAdapter {


    private ArrayList<String> IMAGES;
    private ImageView imageView;
    private ProgressBar progressBar;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);

        System.out.println("calling adapter  :: "+IMAGES.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
            imageView = (ImageView) imageLayout.findViewById(R.id.image);
            progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        try {
            progressBar.setVisibility(View.VISIBLE);
        if(!IMAGES.get(position).equals(""))
        {

            Picasso.with(context)
                    .load(IMAGES.get(position))
                    .error(R.drawable.images_not_found)
                    .placeholder(R.color.gray)
                    .resize(320, 230)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
*/