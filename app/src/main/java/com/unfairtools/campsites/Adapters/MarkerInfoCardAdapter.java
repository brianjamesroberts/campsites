package com.unfairtools.campsites.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.presenters.MarkerInfoFragmentPresenter;
import com.unfairtools.campsites.ui.LocationDetailsFragment;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.LoginManager;
import com.unfairtools.campsites.util.MarkerInfoObject;
import com.unfairtools.campsites.util.OnLoggedInCallback;
import com.unfairtools.campsites.util.SQLMethods;

import javax.inject.Inject;

/**
 * Created by brianroberts on 11/16/16.
 */

public class MarkerInfoCardAdapter extends RecyclerView.Adapter<MarkerInfoCardAdapter.MarkerCardViewHolder> {

    @Inject
    public SQLiteDatabase db;

    @Inject
    public LoginManager lm;


    public Context context;
    private InfoObject markerInfo;
    private MarkerInfoObject info;
    private LocationDetailsFragment locationDetailsFragment;

    public MarkerInfoCardAdapter(MarkerInfoObject incInfoMarkerObject, InfoObject incInfoObject, Context c, LocationDetailsFragment frag){
        this.context = c;
        this.info = incInfoMarkerObject;
        this.markerInfo = incInfoObject;
        this.locationDetailsFragment = frag;

        Log.e("Context is ", context.toString());

        ((BaseApplication)context.getApplicationContext()).getServicesComponent().inject(this);

    }

    public void setData(MarkerInfoObject markerInfoObject, InfoObject infoObject){
        this.markerInfo = infoObject;
        this.info = markerInfoObject;
        this.notifyDataSetChanged();
    }

    public void onBindViewHolder(final MarkerInfoCardAdapter.MarkerCardViewHolder viewHolder, int index){

        //Log.e("Laying out", "LAYOUT INDEX: " + index);

        viewHolder.selfView.setVisibility(View.VISIBLE);

        viewHolder.description.setVisibility(View.GONE);
        viewHolder.description.setText("");
        viewHolder.ratingBar.setVisibility(View.GONE);
        viewHolder.addressTextView.setVisibility(View.GONE);
        viewHolder.buttonLayout.setVisibility(View.GONE);
        viewHolder.switchCompat.setVisibility(View.GONE);
        viewHolder.loginLayout.setVisibility(View.GONE);

        viewHolder.tilesRecycler.setVisibility(View.GONE);

        viewHolder.loadingBar.setVisibility(View.GONE);
        MarkerCardViewHolder.ViewType enumIndex = MarkerCardViewHolder.ViewType.values()[index];

        if(info==null) {
//            if(index!=0) {
//                viewHolder.selfView.setVisibility(View.GONE);
//            }else{
//                viewHolder.loadingBar.setVisibility(View.VISIBLE);
//            }
            viewHolder.selfView.setVisibility(View.GONE);
            return;
        }
        if(enumIndex == MarkerCardViewHolder.ViewType.TYPE_DESCRIPTION) {
            //Log.e("DB IS ", db.toString() + ": info.idprimarykey: " + info.id_primary_key);

            boolean hasMarkerLocal = SQLMethods.existsRecord(SQLMethods.Constants.LOCATIONS_INFO_TABLE_NAME,
                    SQLMethods.Constants.LocationsInfoTable.id_primary_key, info.id_primary_key + "", db);
            //Log.e("MarkerInfCardAdap", "Making descrption visible");
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(info.description);
            viewHolder.switchCompat.setVisibility(View.VISIBLE);
            if (hasMarkerLocal) {
                viewHolder.switchCompat.setChecked(true);
            } else {
                viewHolder.switchCompat.setChecked(false);
            }
            viewHolder.switchCompat.setOnClickListener(new SwitchCompat.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SwitchCompat s = (SwitchCompat) view;
                    if (s.isChecked()) {
                        SQLMethods.addLocationLocal(db, MarkerInfoCardAdapter.this.markerInfo);
                        SQLMethods.addLocationInfoLocal(db, MarkerInfoCardAdapter.this.info);
                        MarkerInfoCardAdapter.this.locationDetailsFragment.refreshMap();
                        Log.e("Adapter", "Saving local " + info.id_primary_key);
                    } else {
                        SQLMethods.deleteLocationLocal(db, markerInfo.ids[0]);
                        MarkerInfoCardAdapter.this.locationDetailsFragment.refreshMap();
                        Log.e("Adapter", "Don't save local " + info.id_primary_key);
                    }
                }
            });
        }else if(enumIndex== MarkerCardViewHolder.ViewType.TYPE_TILES) {

            //Log.e("MarkerInfoCardAdap","enumIndex TYPE TILES");
            viewHolder.tilesRecycler.setVisibility(View.VISIBLE);
            if(viewHolder.tilesRecycler.getAdapter()==null){
                //Log.e("ADAPTER", "ADAPTER WAS NULL!!!");
                viewHolder.tilesRecycler.setAdapter(new ImageTilesAdapter());
                ((ImageTilesAdapter)viewHolder.tilesRecycler.getAdapter()).setData(info);
            }
        }else if(enumIndex== MarkerCardViewHolder.ViewType.TYPE_RATING_COMMENTS){

                    if(MarkerInfoCardAdapter.this.lm.isLoggedIn()){
                        viewHolder.ratingBar.setVisibility(View.VISIBLE);
                    }else{
                        viewHolder.loginLayout.setVisibility(View.VISIBLE);
                        viewHolder.loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lm.newLogin(viewHolder.userText.getText().toString(), viewHolder.passText.getText().toString(),
                                        new OnLoggedInCallback() {
                                            @Override
                                            public void onFinish() {
                                                String authKey =  SQLMethods.getLastAuthKey(db);
                                                Log.e("LoginManager", "Login received " + authKey + " logged in?: " + lm.isLoggedIn());
                                                notifyDataSetChanged();
                                            }
                                        });
                            }
                        });
                    }


        }else if(enumIndex==MarkerCardViewHolder.ViewType.TYPE_ADDRESS){
            viewHolder.addressTextView.setVisibility(View.VISIBLE);
            viewHolder.addressTextView.setText(info.address);
        }else if(enumIndex==MarkerCardViewHolder.ViewType.TYPE_WEBSITES){
            viewHolder.buttonLayout.setVisibility(View.VISIBLE);
            viewHolder.webButton.setOnClickListener(new AppCompatImageButton.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(info.website));
                    context.startActivity(i);
                }
            });
            viewHolder.googleButton.setOnClickListener(new AppCompatImageButton.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    //Log.e("MICAdapter","google_url: " + info.google_url);
                    i.setData(Uri.parse(info.google_url));
                    context.startActivity(i);

                }
            });
        }
    }



//    public void setInfo(MarkerInfoObject in){
//        this.info = in;
//    }


    @Override
    public int getItemCount(){
        return 5;
    }


    @Override
    public MarkerCardViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marker_card_layout, parent, false);

        MarkerCardViewHolder dataObjectHolder = new MarkerCardViewHolder(view);

        return dataObjectHolder;
    }

    public static class MarkerCardViewHolder extends RecyclerView.ViewHolder{

        RecyclerView tilesRecycler;
        SwitchCompat switchCompat;
        TextView description;
        RatingBar ratingBar;
        TextView addressTextView;
        LinearLayout loginLayout;

        LinearLayout buttonLayout;
        AppCompatImageButton webButton;
        AppCompatImageButton googleButton;

        EditText userText;
        EditText passText;
        Button loginButton;

        LinearLayout selfView;

        ProgressBar loadingBar;

        public MarkerCardViewHolder(View itemView) {

            super(itemView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);

            tilesRecycler = (RecyclerView)itemView.findViewById(R.id.tiles_recycler_view);;
            tilesRecycler.setLayoutManager(layoutManager);
            loginLayout = (LinearLayout)itemView.findViewById(R.id.login_layout_card_layout);
            description = (TextView)itemView.findViewById(R.id.card_view_description_text);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            addressTextView = (TextView)itemView.findViewById(R.id.card_view_address_text);
            buttonLayout = (LinearLayout)itemView.findViewById(R.id.web_button_layout);
            webButton = (AppCompatImageButton)itemView.findViewById(R.id.web_button);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.switchCompat);
            googleButton = (AppCompatImageButton) itemView.findViewById(R.id.google_button);
            loginButton = (Button)itemView.findViewById(R.id.login_button_card);
            userText = (EditText)itemView.findViewById(R.id.user_text_card);
            passText = (EditText)itemView.findViewById(R.id.pass_text_card);
            selfView = (LinearLayout) itemView.findViewById(R.id.card_view_parent);
            loadingBar = (ProgressBar) itemView.findViewById(R.id.card_progress_bar);
        }


        public enum ViewType{
            TYPE_DESCRIPTION,
            TYPE_ADDRESS,
            TYPE_WEBSITES,
            TYPE_TILES,
            TYPE_RATING_COMMENTS
        }

    }

}
