package com.fdanesse.jamedia;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fdanesse.jamedia.Archivos.FileChooserItemListAdapter;

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
        view.setAlpha(0.5f);
    }
}
