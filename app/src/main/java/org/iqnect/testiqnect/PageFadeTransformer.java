package org.iqnect.testiqnect;


import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class PageFadeTransformer implements PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        if(position <= -1.0F || position >= 1.0F) {
            page.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            page.setAlpha(1.0F);
        } else {
            page.setAlpha(1.0F - Math.abs(position));
        }
    }
}
