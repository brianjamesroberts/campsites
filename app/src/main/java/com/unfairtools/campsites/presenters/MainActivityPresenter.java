package com.unfairtools.campsites.presenters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.contracts.MainContract;
import com.unfairtools.campsites.ui.MainActivity;
import com.unfairtools.campsites.util.ApiService;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by brianroberts on 11/15/16.
 */

public class MainActivityPresenter implements MainContract.Presenter {

    @Inject
    ApiService apiService;

    MainContract.View view;
    BaseApplication base;

    PopupWindow pwindow;

    private AutoCompleteTextView autoCompleteTextView;


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
        //pwindow.dismiss();
        animator.setDuration(MainActivity.ToolbarAnimationTimeMS);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

    }




    //called by the view when the layout has been laid-out
    public void notifyViewIsReady(){
        autoCompleteTextView = this.view.getToolbarEditText();


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("Text is", charSequence.toString());

                if(view.getToolbarEditText().isEnabled() && charSequence.length()>0){
                    view.getClearTextButton().setVisibility(View.VISIBLE);
                }else{
                    view.getClearTextButton().setVisibility(View.GONE);
                }
                if(charSequence.length()>0 && view.getToolbarEditText().isEnabled()){
                    view.getClearTextButton().setVisibility(View.VISIBLE);
                    view.getClearTextButton().invalidate();
                }else{
                    view.getClearTextButton().setVisibility(View.GONE);
                    view.getClearTextButton().invalidate();
                }

                if(charSequence.length()<2) {
                    if(pwindow!=null && pwindow.isShowing()) {
                        pwindow.dismiss();
                    }
                    return;
                }
                Object tag = MainActivityPresenter.this.view.getToolbarEditText().getTag();
                if(tag!=null && tag.equals("0")){
                    Log.e("TAG","Tag was " + tag.toString() );
                    MainActivityPresenter.this.view.getToolbarEditText().setTag("1");
                    return;
                }
                final InfoObject infoObjectInput = new InfoObject();
                infoObjectInput.name = charSequence.toString();
                Call<InfoObject> call = apiService.postForSearchResults(infoObjectInput);
                call.enqueue(new Callback<InfoObject>() {
                    @Override
                    public void onResponse(Call<InfoObject> call, retrofit2.Response<InfoObject> response) {
                        try {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            System.out.println("received json: " + json);
                            if (response.isSuccessful()) {
                                final InfoObject inf = response.body();
                                if (response.body().names!= null) {
                                    //System.out.println(response.body().names);
                                    Handler mainHandler = new Handler(view.getViewContext().getMainLooper());
                                    Runnable myRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivityPresenter.this.takeSearchSuggestions(inf);
                                        } // This is your code
                                    };
                                    mainHandler.post(myRunnable);



                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<InfoObject> call, Throwable t) {
                        Log.e("resp", "failed " + t.toString() + " is executed: " + call.isExecuted());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    public void takeSearchSuggestions(InfoObject infoObjectInput){


        if(infoObjectInput==null ||infoObjectInput.names==null || infoObjectInput.names.length==0)
            return;




        ArrayList<InfoObject> results = new ArrayList<InfoObject>();
        Log.e("Info", "InfoObjectInput.names length: " + infoObjectInput.names.length);
        for(int i = 0 ; i < infoObjectInput.names.length;i++){
            InfoObject inf = new InfoObject();
            if(infoObjectInput.names[i]==null)
                continue;
            inf.name = infoObjectInput.names[i];
            inf.latPoint = infoObjectInput.latitudes[i];
            inf.longPoint = infoObjectInput.longitudes[i];
            inf.ids = new int[]{infoObjectInput.ids[i]};
            results.add(inf);
        }


        final int popupwindowHeight = LinearLayout.LayoutParams.WRAP_CONTENT;

        LayoutInflater layoutInflater = (LayoutInflater) ((Activity)view.getViewContext())
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(
                R.layout.window_suggestions, null);

// Creating the PopupWindow
        if(pwindow==null) {
            pwindow = new PopupWindow(view.getViewContext());
            pwindow .setContentView(layout);
            pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pwindow = null;
                }
            });
        }else{
            pwindow.setContentView(layout);
        }


        TextView[] editTexts = new TextView[]{((TextView)pwindow.getContentView().findViewById(R.id.window_text1)),
                ((TextView)pwindow.getContentView().findViewById(R.id.window_text2)),
                ((TextView)pwindow.getContentView().findViewById(R.id.window_text3)),
                ((TextView)pwindow.getContentView().findViewById(R.id.window_text4)),
                ((TextView)pwindow.getContentView().findViewById(R.id.window_text5))};

        for(TextView t: editTexts){
            t.setText("");
            t.setVisibility(View.GONE);
        }

        Log.e("Attempting", " to add names to edit texts " + results.size() + ", " + editTexts.length);
        for(int i = 0; i < results.size() && i < editTexts.length; i++){
            Log.e("MainActPresent", "adding " + results.get(i).name + " to edit texts");
            editTexts[i].setText(results.get(i).name);
            editTexts[i].setVisibility(View.VISIBLE);
            editTexts[i].setTag(R.string.tag_latitude,results.get(i).latPoint);
            editTexts[i].setTag(R.string.tag_longitude,results.get(i).longPoint);
            editTexts[i].setTag(R.string.tag_id,results.get(i).ids[0]);
            editTexts[i].setTag(R.string.tag_name,results.get(i).name);
            editTexts[i].setOnClickListener(new TextView.OnClickListener(){
                public void onClick(View v){
                   Double latitude =(double) ((TextView)v).getTag(R.string.tag_latitude);
                    Double longitude = (double)((TextView)v).getTag(R.string.tag_longitude);
                    int id = (int) ((TextView)v).getTag(R.string.tag_id);
                    String name = (String) ((TextView)v).getTag(R.string.tag_name);
                    Log.e("MainActPresenter","id: " + id +", lat: " + latitude + ", long: " +longitude + ", name: " + name  );
                    view.getToolbarEditText().setTag("0");
                    view.getToolbarEditText().setText(name);
                    view.getToolbarEditText().clearFocus();
                    view.hideKeyboard();
                    pwindow.dismiss();
                    view.getMapsPresenter().setShowTagId(id);
                    view.getMapsPresenter().sendMapTo(latitude,longitude);

                }
            });
        }



        pwindow.setOutsideTouchable(true);
        int width = view.getToolbarEditText().getWidth();
        pwindow.setWidth(width);
        pwindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        pwindow.update();
        pwindow.showAsDropDown(view.getToolbarEditText());



    }

    public MainActivityPresenter(MainContract.View v, BaseApplication b){
        this.view = v;
        this.base = b;

        this.base.getServicesComponent().inject(this);

    }
}
