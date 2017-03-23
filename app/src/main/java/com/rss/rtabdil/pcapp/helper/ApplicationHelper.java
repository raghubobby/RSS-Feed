package com.rss.rtabdil.pcapp.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by rtabdil on 3/19/17.
 */

public class ApplicationHelper {

    private Context mContext;
    private static final String TAG = ApplicationHelper.class.getName();


    public ApplicationHelper(Context context) {
        mContext = context;
    }

    public boolean isConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTablet(Context mContext) {
        boolean xlarge = ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);

    }

}
