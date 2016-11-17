package com.unfairtools.campsites.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.unfairtools.campsites.util.MarkerInfoObject;

/**
 * Created by brianroberts on 11/16/16.
 */

public class MarkerInfoCardAdapter extends RecyclerView.Adapter<MarkerInfoCardAdapter.MarkerCardViewHolder> {

    public Context context;

    public void onBindViewHolder(MarkerInfoCardAdapter.MarkerCardViewHolder viewHolder, int index){

        viewHolder.description.setVisibility(View.GONE);
        viewHolder.description.setText("");
        viewHolder.ratingBar.setVisibility(View.GONE);
        viewHolder.progressBar.setVisibility(View.GONE);
        viewHolder.addressTextView.setVisibility(View.GONE);
        viewHolder.buttonLayout.setVisibility(View.GONE);
        MarkerCardViewHolder.ViewType enumIndex = MarkerCardViewHolder.ViewType.values()[index];
        if(enumIndex == MarkerCardViewHolder.ViewType.TYPE_DESCRIPTION) {
            Log.e("MarkerInfCardAdap","Making descrption visible");
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(info.description);
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

    private MarkerInfoObject info;


    public void setInfo(MarkerInfoObject in){
        this.info = in;
    }

    public MarkerInfoCardAdapter(MarkerInfoObject inc, Context c){
        this.context = c;
        this.info = inc;
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
            googleButton = (AppCompatImageButton) itemView.findViewById(R.id.google_button);
        }


        public enum ViewType{
            TYPE_DESCRIPTION,
            TYPE_ADDRESS,
            TYPE_WEBSITES,
            TYPE_TILES,
            TYPE_RATING_COMMENTS,



        }

    }

}
