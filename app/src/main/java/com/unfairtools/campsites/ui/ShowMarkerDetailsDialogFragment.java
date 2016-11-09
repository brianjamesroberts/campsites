package com.unfairtools.campsites.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.dagger.component.DaggerMapsComponent;
import com.unfairtools.campsites.dagger.component.DaggerMarkerInfoFragmentComponent;
import com.unfairtools.campsites.dagger.module.MapsModule;
import com.unfairtools.campsites.dagger.module.MarkerInfoFragmentModule;
import com.unfairtools.campsites.dagger.module.RealmModule;
import com.unfairtools.campsites.maps.MarkerInfoContract;
import com.unfairtools.campsites.maps.MarkerInfoDialogFragmentPresenter;
import com.unfairtools.campsites.util.InfoObject;

import javax.inject.Inject;


public class ShowMarkerDetailsDialogFragment extends DialogFragment implements MarkerInfoContract.View {
    // TODO: Rename parameter arguments, choose names that match


    private OnFragmentInteractionListener mListener;

    public ShowMarkerDetailsDialogFragment() {
        // Required empty public constructor
    }

    @Inject
    MarkerInfoDialogFragmentPresenter presenter;


    ProgressBar view_progressBar;
    TextView view_name;



    public void takeInfo(InfoObject inf){

    }

    public void takePrelimInfo(InfoObject inf){

        view_progressBar.setIndeterminate(true);

        view_name.setText(inf.name);

        Log.e("ShowMDDF", "name: "  + inf.name);




    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_show_marker_details_dialog, null);



        DaggerMarkerInfoFragmentComponent.builder()
                .markerInfoFragmentModule(new MarkerInfoFragmentModule(this,(BaseApplication)getActivity().getApplication()))
                //.realmModule(new RealmModule((BaseApplication)getActivity().getApplication()))
                .build()
                .inject(this);


        view_progressBar = (ProgressBar)v.findViewById(R.id.progress_bar_marker_info);

        view_name = (TextView)v.findViewById(R.id.title_text_marker_info);

        presenter.setMarkerIdAndName(getArguments().getInt("id"), getArguments().getString("name"));




        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

     //TODO: Rename and change types and number of parameters
    public static ShowMarkerDetailsDialogFragment newInstance(int idd, String namee){
        ShowMarkerDetailsDialogFragment fragment = new ShowMarkerDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", idd);
        args.putString("name",namee);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public int getTheme() {
        Log.e("ShowMDDF","getTheme called");
        return R.style.DialogAnimation;
    }
}
