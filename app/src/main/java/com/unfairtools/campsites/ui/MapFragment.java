package com.unfairtools.campsites.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.maps.MapsContract;


public class MapFragment extends SupportMapFragment implements MapsContract.View {
    // TODO: Rename parameter arguments, choose names that match


    private static String map_api_key = "AIzaSyC7wvENi9-UK9ateIMJ0xPleu_ZKqst7lU";

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

//    public static MapFragment newInstance(String param1, String param2) {
//        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewMain = super.onCreateView(inflater, container, savedInstanceState);

        return viewMain;
        //return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
