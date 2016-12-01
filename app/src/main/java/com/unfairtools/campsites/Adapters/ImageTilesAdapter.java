package com.unfairtools.campsites.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.unfairtools.campsites.R;
import com.unfairtools.campsites.util.MarkerInfoObject;

/**
 * Adapter for the descriptive item tiles found in a recycler view in LocationDetailsFrragment instance.
 * Created by brianroberts on 11/30/16.
 */

public class ImageTilesAdapter extends RecyclerView.Adapter<ImageTilesAdapter.ImageTile>{

    public int[] ids = new int[0];

    public void setData(MarkerInfoObject inf){

        if(inf==null || inf.facilities==null)
            return;
        String[] strArray = inf.facilities.split(",");
        this.ids = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            this.ids[i] = Integer.parseInt(strArray[i]);
        }
        this.notifyDataSetChanged();
    }
    //MarkerInfoObject info;
    @Override
    public ImageTile onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tile_image, parent, false);

        ImageTilesAdapter.ImageTile imageTile = new ImageTilesAdapter.ImageTile(view);
        return imageTile;
    }

    @Override
    public void onBindViewHolder(ImageTile holder, int position) {
        //Log.e("Laying out", position + "");
            switch(ids[position]){
                case 0:
                    holder.imageView.setBackgroundResource(R.drawable.ic_tiles_dollarsign_48dp);
                    holder.imageView.setOnClickListener(new ImageView.OnClickListener(){
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case 1:
                    holder.imageView.setBackgroundResource(R.drawable.ic_tiles_dollarsign_48dp);
                    break;
                default:
                    holder.imageView.setBackgroundResource(R.drawable.ic_action_gps);
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return ids.length;
    }

    static class ImageTile extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ImageTile(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_tile_image_view);
        }
    }

}

