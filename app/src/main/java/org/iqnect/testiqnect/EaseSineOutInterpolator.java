package org.iqnect.testiqnect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;


import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * Created by cimi on 15/7/3.
 */

public class EaseSineOutInterpolator implements Interpolator {

    private static final float PI = (float) Math.PI;
    private static float _HALF_PI = PI * 0.5f;

    public EaseSineOutInterpolator() {}

    public EaseSineOutInterpolator(Context context, AttributeSet attrs) {}

    public float getInterpolation(float input) {
        return (float) Math.sin(input * _HALF_PI);
    }
}