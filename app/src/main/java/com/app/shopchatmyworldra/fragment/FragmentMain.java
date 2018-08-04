package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shopchatmyworldra.BuyActivity;
import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.SellActivity;
import com.app.shopchatmyworldra.constant.CustomViewPager;

/**
 * Created by legacy on 06-Nov-17.
 */

public class FragmentMain extends Fragment {
    private View view;
    private Activity mactivity;
    private TabLayout tabLayout;
    public static CustomViewPager viewPager;
    ViewPagerAdapter adapter;

    private FragmentManager fragmentManager;
    private Fragment fragment = null;

    public FragmentMain() {
    }

    @Override
    public void onAttach(Activity activity) {
        mactivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);


        viewPager = (CustomViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        MainActivity.linemain.setVisibility(View.GONE);

        ViewPagerAdapter adapter = new ViewPagerAdapter
                (getFragmentManager(), mactivity);
        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Intent intent = new Intent(mactivity, BuyActivity.class);
                    mactivity.startActivity(intent);
                }if (position == 2) {
                    Intent intent = new Intent(mactivity, SellActivity.class);
                    mactivity.startActivity(intent);
                }

                //Log.e("checkterminate","onPageSelected="+position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here

            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(mactivity,"Pos onTabSelected: "+ tab.getSelectedTabPosition(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {
                    Intent intent = new Intent(mactivity, BuyActivity.class);
                    mactivity.startActivity(intent);
                }if (tab.getPosition() == 2) {
                    Intent intent = new Intent(mactivity, SellActivity.class);
                    mactivity.startActivity(intent);
                }

            }
        });

        viewPager.setPagingEnabled(false);

        fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new FragmentMain();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {
            //Do your stuff here

        }
        super.setMenuVisibility(visible);
    }


    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(mactivity).inflate(R.layout.custom_tab, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.house, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(mactivity).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Buy");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.buy_white, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(mactivity).inflate(R.layout.custom_tab, null);
        tabThree.setText("Sell");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sell_white, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(mactivity).inflate(R.layout.custom_tab, null);
        tabFour.setText("Wishlist");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wishlist, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
        viewPager.setOffscreenPageLimit(4);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitles;
        Activity mctivity;

        public ViewPagerAdapter(FragmentManager fm, Activity mctivity) {
            super(fm);
            this.mctivity = mctivity;
            tabTitles = mctivity.getResources().getStringArray(R.array.my_array);
        }

        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentHome();
                case 1:
                    return new FragmentBuy();
                case 2:
                    return new FragmentSell();
                case 3:
                    return new FragmentWishList();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}