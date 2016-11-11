package com.unfairtools.campsites.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unfairtools.campsites.MainActivity;
import com.unfairtools.campsites.R;
import com.unfairtools.campsites.base.BaseApplication;
import com.unfairtools.campsites.dagger.component.DaggerMarkerInfoFragmentComponent;
import com.unfairtools.campsites.dagger.module.MarkerInfoFragmentModule;
import com.unfairtools.campsites.maps.MapsContract;
import com.unfairtools.campsites.maps.MarkerInfoContract;
import com.unfairtools.campsites.maps.MarkerInfoFragmentPresenter;
import com.unfairtools.campsites.util.InfoObject;
import com.unfairtools.campsites.util.MarkerInfoObject;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationDetailsFragment extends Fragment implements MarkerInfoContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @Inject
    MarkerInfoFragmentPresenter presenter;



    public void takeInfo(MarkerInfoObject inf){

        ((ProgressBar)getView().findViewById(R.id.progress_bar_marker_info)).setVisibility(View.GONE);
        ((TextView)getView().findViewById(R.id.title_text_marker_info)).setText(inf.description);

    }

    public void takePrelimInfo(InfoObject inf){

        Log.e("ShowMDDF", "my id: "  + inf.ids[0]);


    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainActivity getMainActivity(){
        return (MainActivity)getActivity();
    }

    public LocationDetailsFragment() {
        // Required empty public constructor
    }


    public static LocationDetailsFragment newInstance(Integer id) {
        LocationDetailsFragment fragment = new LocationDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = -1;
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }

        DaggerMarkerInfoFragmentComponent.builder()
                .markerInfoFragmentModule(new MarkerInfoFragmentModule(this, (BaseApplication)getActivity().getApplication()))
                .build()
                .inject(this);

        presenter.setMarkerIdAndName(id);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_details, container, false);
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
        getMainActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getMainActivity().getSupportActionBar().setHomeButtonEnabled(false);
        getMainActivity().getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getMainActivity().setToggleHamburgerVisibility(true);
        Log.e("LocationDetailsFrag", "onDetach called");
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
