package com.unfairtools.campsites.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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

    public static class OnClickListener implements View.OnClickListener{


        String data;
        ImageTile holder;
        public OnClickListener(String s, ImageTile imageTile){
            data = s;
            holder = imageTile;
        }

        @Override
        public void onClick(View view) {
            Log.e(data, data);
            View layout = LayoutInflater.from(holder.imageView.getContext()).inflate(R.layout.single_window_suggestions, null);

            PopupWindow pwindow = new PopupWindow(holder.imageView.getContext());
            pwindow.setContentView(layout);

            ((TextView) pwindow.getContentView().findViewById(R.id.window_text1)).setText(data);

            pwindow.setOutsideTouchable(true);

            pwindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            pwindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pwindow.update();
            pwindow.showAsDropDown(holder.imageView);

        }
    }

    @Override
    public void onBindViewHolder(ImageTile holder, int position) {
        //Log.e("Laying out", position + "");
//            switch(ids[position]){

                holder.imageView.setBackgroundResource(getTileImgId(ids[position]));
        holder.imageView.setOnClickListener(new ImageTilesAdapter.OnClickListener(getTileImgLabel(ids[position]),holder));

//                case 0:
//                    holder.imageView.setBackgroundResource(R.drawable.ic_tiles_dollarsign_48dp);
//                    holder.imageView.setOnClickListener(new ImageView.OnClickListener(){
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });
//                    break;
//                case 1:
//
//                    holder.imageView.setBackgroundResource(R.drawable.ic_tiles_dollarsign_48dp);
//                    break;
//                default:
//                    holder.imageView.setBackgroundResource(R.drawable.ic_action_gps);
//                    break;
//        }
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


    public String getTileImgLabel(int input){
        switch(input){
            case -1:
                return "caution: there is a problem with this campground.";
            case 0:
                return "null";
            case 1:
                return "tent camping";
            case 2:
                return "bathroom available";
            case 3:
                return "r.v. camping";
            case 4:
                return "handicap accessible";
            case 5:
                return "costs money";
            case 6:
                return "potable water available";
            case 7:
                return "first come first serve";
            case 8:
                return "reservations available/required";
            case 9:
                return "vehicle access";
            case 10:
                return "free campsite";
            case 11:
                return "no vehicle access";
            default:
                return "null";
        }
    }

    public int getTileImgId(int input){
        switch(input) {
            case -1:
                return R.drawable.ic_close_black;
            case 0:
                return R.drawable.ic_action_gps;
            case 1:
                return R.drawable.ic_tent;
            case 2:
                return R.drawable.ic_bathroom;
            case 3:
                return R.drawable.ic_rv;
            case 4:
                return R.drawable.ic_handicap;
            case 5:
                return R.drawable.ic_tiles_dollarsign_48dp;
            case 6:
                return R.drawable.ic_water;
            case 7:
                return R.drawable.ic_firstcomefirstserve;
            case 8:
                return R.drawable.ic_reservations;
            case 9:
                return R.drawable.ic_car;
            case 10:
                return R.drawable.ic_no_money;
            case 11:
                return R.drawable.ic_no_car;
            default:
                return R.drawable.ic_action_gps;
        }
    }

}

