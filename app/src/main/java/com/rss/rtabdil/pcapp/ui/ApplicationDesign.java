package com.rss.rtabdil.pcapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;

import com.rss.rtabdil.pcapp.R;

/**
 * Created by rtabdil on 3/21/17.
 */

public class ApplicationDesign {

    Context mContext;

    public ApplicationDesign(Context mContext) {
        mContext = mContext;
    }

    public Toolbar createToolbar(Activity activity) {

        Toolbar toolbar = new Toolbar(activity);
        LinearLayout.LayoutParams toolbarParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpTopxs(50,activity));
        toolbar.setLayoutParams(toolbarParams);
        toolbar.setBackgroundColor(Color.BLUE);
        int toolBarBackground = activity.getResources().getColor(R.color.colorPrimary);
        toolbar.setBackgroundColor(toolBarBackground);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setVisibility(View.VISIBLE);
        return toolbar;

    }

    public int dpTopxs(int dps, Activity activity) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

}
