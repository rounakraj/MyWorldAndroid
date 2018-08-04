package com.app.shopchatmyworldra.constant;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.shopchatmyworldra.R;

public class ProgressBarDialog {


    public static Dialog dialog;
 //   private static CircularProgressBar mCircularProgressBar;
    private static ImageView ivLoad;

    public static void showProgressBar(final Activity activity, String title) {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
                return;
            }
        }

        try {
            // if ("".equalsIgnoreCase(title)) {
            // title = "Alert";
            // }
            title = "Alert";
            dialog = new Dialog(activity,
                    android.R.style.Theme_Translucent_NoTitleBar);
            dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialog_AppCompat;
            dialog.setContentView(R.layout.progressbar_dialog);

            FrameLayout frameLayout = (FrameLayout) dialog
                    .findViewById(R.id.rv);
            //new ASSL(activity, frameLayout, 1134, 720, true);

            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

         //   mCircularProgressBar = (CircularProgressBar) dialog.findViewById(R.id.progressbar_circular);
           // updateValues(activity);

            ivLoad = (ImageView) dialog.findViewById(R.id.ivLoad);
           // Glide.with(activity).load(R.raw.loader).into(ivLoad);
            ivLoad.setVisibility(View.GONE);
         //   ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();

            if(dialog!=null && !dialog.isShowing())
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* private static void updateValues(Activity activity) {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable
                .Builder(activity)
                //	.colors(activity.getResources().getIntArray(R.array.gplus_colors))
//				.sweepSpeed(mSpeed)
//				.rotationSpeed(mSpeed)
//				.strokeWidth(2)
                .style(CircularProgressDrawable.Style.ROUNDED);
//		if (mCurrentInterpolator != null) {
//			b.sweepInterpolator(mCurrentInterpolator);
//		}
        mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0, 0,
                mCircularProgressBar.getWidth(),
                mCircularProgressBar.getHeight());
        mCircularProgressBar.setVisibility(View.INVISIBLE);
        mCircularProgressBar.setVisibility(View.VISIBLE);
    }*/

    public static void dismissProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {

                dialog.dismiss();
                dialog=null;
            }
        }
    }

}
