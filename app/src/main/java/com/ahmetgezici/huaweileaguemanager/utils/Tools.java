package com.ahmetgezici.huaweileaguemanager.utils;

import android.content.res.Resources;

public class Tools {

    public static int dpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(double px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
