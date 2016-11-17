package com.unfairtools.campsites.presenters;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MainContract;
import com.unfairtools.campsites.ui.MainActivity;

/**
 * Created by brianroberts on 11/15/16.
 */

public class MainActivityPresenter implements MainContract.Presenter {

    MainContract.View view;
    BaseApplication base;


    public void animateToolbarMargin(final int margin){

        Log.e("MainActPresenter","AnimateToolBarMargin: " + margin);
        final FrameLayout tb = view.getToolbarFrameLayout();

        if(margin==0)
            view.getToolbarEditText().setTextSize(18);
        else
            view.getToolbarEditText().setTextSize(16);
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)tb.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin, margin);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue();
                params.rightMargin = (Integer) valueAnimator.getAnimatedValue();
                tb.requestLayout();
            }
        });
        animator.setDuration(MainActivity.ToolbarAnimationTimeMS);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

    }

    public MainActivityPresenter(MainContract.View v, BaseApplication b){
        view = v;
        base = b;
    }
}
