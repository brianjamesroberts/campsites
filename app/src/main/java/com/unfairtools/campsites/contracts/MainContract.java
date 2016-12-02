package com.unfairtools.campsites.contracts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unfairtools.campsites.base.BasePresenter;
import com.unfairtools.campsites.base.BaseView;
import com.unfairtools.campsites.presenters.MapsPresenter;

/**
 * Created by brianroberts on 11/15/16.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

        public FrameLayout getToolbarFrameLayout();
        public AutoCompleteTextView getToolbarEditText();
        public Context getViewContext();
        public MapsContract.Presenter getMapsPresenter();
        public void hideKeyboard();
        public ImageButton getClearTextButton();
        public RecyclerView getToolbarRecyclerView();
        public TextView getToolbarDisplayOnlyEditText();


    }

    interface Presenter extends BasePresenter {

        public void animateToolbarMargin(int margin);
        public void notifyViewIsReady();

    }
}
