package com.fdanesse.jamedia.Youtube;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by flavio on 19/01/17.
 */
public class DeactivatableViewPager2 extends ViewPager {
    public DeactivatableViewPager2(Context context) {
        super(context);
    }

    public DeactivatableViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isEnabled() || super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isEnabled() && super.onInterceptTouchEvent(event);
    }
}
