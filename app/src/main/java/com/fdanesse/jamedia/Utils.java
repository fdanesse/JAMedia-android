package com.fdanesse.jamedia;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by flavio on 07/10/16.
 */
public class Utils {

    public static void setActiveView(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), R.anim.anim_alpha2);
        view.startAnimation(animAlpha);
        view.setAlpha(1.0f);
    }

    public static void setInactiveView(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), R.anim.anim_alpha1);
        view.startAnimation(animAlpha);
        view.setAlpha(0.2f);
    }

    public static void setActiveView2(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), R.anim.anim_alpha4);
        view.startAnimation(animAlpha);
        view.setAlpha(1.0f);
    }

    public static void setInactiveView2(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), R.anim.anim_alpha3);
        view.startAnimation(animAlpha);
        view.setAlpha(0.5f);
    }
}
