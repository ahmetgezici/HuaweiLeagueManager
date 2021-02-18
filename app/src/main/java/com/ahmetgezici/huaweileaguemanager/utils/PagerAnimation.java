package com.ahmetgezici.huaweileaguemanager.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class PagerAnimation implements ViewPager2.PageTransformer {

    private static final float MIN_SCALE = 0.65f;
    private static final float MIN_ALPHA = 0.3f;

    @Override
    public void transformPage(@NonNull View page, float position) {

        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 1) {

            float max = Math.max(MIN_SCALE, 1 - Math.abs(position));
            page.setScaleX(max);
            page.setScaleY(max);
            page.setAlpha(Math.max(MIN_ALPHA, 1 - Math.abs(position)));

        } else {
            page.setAlpha(0);
        }
    }
}