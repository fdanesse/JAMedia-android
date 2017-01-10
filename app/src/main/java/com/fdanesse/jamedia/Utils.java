package com.fdanesse.jamedia;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by flavio on 07/10/16.
 */


public class Utils {

    public static void setActiveView(View view, String type){
        int anim = R.anim.default_active_anim;
        float alpha = 1.0f;

        switch (type){
            case "filechooser_items": {
                anim = R.anim.filechooser_items_active_anim;
                alpha = 0.7f;
                break;
            }
        }

        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), anim);
        view.startAnimation(animAlpha);
        view.setAlpha(alpha);
    }

    public static void setInactiveView(View view, String type){
        int anim = R.anim.defaul_inactive_anim;
        float alpha = 0.5f;

        switch (type){
            case "filechooser_toolbar_buttons": {
                anim = R.anim.filechooser_toolbar_buttons_inactive_anim;
                alpha = 0.4f;
                break;
            }
            case "filechooser_items": {
                anim = R.anim.filechooser_items_inactive_anim;
                alpha = 0.4f;
                break;
            }
        }

        final Animation animAlpha = AnimationUtils.loadAnimation(
                view.getContext(), anim);
        view.startAnimation(animAlpha);
        view.setAlpha(alpha);
    }

}
