package com.unfairtools.campsites.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.presenters.MarkerInfoFragmentPresenter;
import com.unfairtools.campsites.ui.LocationDetailsFragment;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.LoginManager;
import com.unfairtools.campsites.util.MarkerInfoObject;
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

        ((BaseApplication)context.getApplicationContext()).getServicesComponent().inject(this);

    }

    public void onBindViewHolder(MarkerInfoCardAdapter.MarkerCardViewHolder viewHolder, int index){





        viewHolder.description.setVisibility(View.GONE);
        viewHolder.description.setText("");
        viewHolder.ratingBar.setVisibility(View.GONE);
        viewHolder.progressBar.setVisibility(View.GONE);
        viewHolder.addressTextView.setVisibility(View.GONE);
        viewHolder.buttonLayout.setVisibility(View.GONE);
        viewHolder.switchCompat.setVisibility(View.GONE);
        MarkerCardViewHolder.ViewType enumIndex = MarkerCardViewHolder.ViewType.values()[index];
        if(enumIndex == MarkerCardViewHolder.ViewType.TYPE_DESCRIPTION) {
            boolean hasMarkerLocal = hasMarkerLocal = SQLMethods.existsRecord(SQLMethods.Constants.LOCATIONS_INFO_TABLE_NAME,
                    SQLMethods.Constants.LocationsInfoTable.id_primary_key,info.id_primary_key + "",db);
            Log.e("MarkerInfCardAdap","Making descrption visible");
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(info.description);
            viewHolder.switchCompat.setVisibility(View.VISIBLE);
            if(hasMarkerLocal)
                viewHolder.switchCompat.setChecked(true);
            else
                viewHolder.switchCompat.setChecked(false);
            viewHolder.switchCompat.setOnClickListener(new SwitchCompat.OnClickListener(){
                @Override
                public void onClick(View view) {
                    SwitchCompat s = (SwitchCompat)view;
                    if(s.isChecked()){
                        SQLMethods.addLocationLocal(db,MarkerInfoCardAdapter.this.markerInfo);
                        SQLMethods.addLocationInfoLocal(db,MarkerInfoCardAdapter.this.info);
                        MarkerInfoCardAdapter.this.locationDetailsFragment.refreshMap();
                        Log.e("Adapter","Saving local " + info.id_primary_key);
                    }else{
                        SQLMethods.deleteLocationLocal(db,markerInfo.ids[0]);
                        MarkerInfoCardAdapter.this.locationDetailsFragment.refreshMap();
                        Log.e("Adapter","Don't save local " + info.id_primary_key);
                    }
                }
            });
        }else if(enumIndex== MarkerCardViewHolder.ViewType.TYPE_RATING_COMMENTS){
            viewHolder.ratingBar.setVisibility(View.VISIBLE);
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
                    i.setData(Uri.parse(info.google_url));
                    context.startActivity(i);

                }
            });
        }
    }



    public void setInfo(MarkerInfoObject in){
        this.info = in;
    }


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

        SwitchCompat switchCompat;
        TextView description;
        RatingBar ratingBar;
        ProgressBar progressBar;
        TextView addressTextView;

        LinearLayout buttonLayout;
        AppCompatImageButton webButton;
        AppCompatImageButton googleButton;

        public MarkerCardViewHolder(View itemView) {
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.card_view_description_text);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_marker_info);
            addressTextView = (TextView)itemView.findViewById(R.id.card_view_address_text);
            buttonLayout = (LinearLayout)itemView.findViewById(R.id.web_button_layout);
            webButton = (AppCompatImageButton)itemView.findViewById(R.id.web_button);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.switchCompat);
            googleButton = (AppCompatImageButton) itemView.findViewById(R.id.google_button);
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
