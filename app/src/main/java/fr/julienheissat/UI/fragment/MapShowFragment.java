package fr.julienheissat.ui.fragment;


import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.modelcontroller.LocationController;
import fr.julienheissat.taskmanager.R;


public class MapShowFragment extends Fragment implements LocationController.LocationControllerListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnMapFragmentInteractionListener mListener;
    private ProgressBar mActivityIndicator;
    private TextView mAddress;
    private LocationController locationController;
    private GoogleMap mMap;
    private TaskManagerApplication app;
    private boolean addressRetrieved;



    public MapShowFragment() {}


    // TODO: Rename and change types and number of parameters
    public static MapShowFragment newInstance(String param1, String param2) {
        MapShowFragment fragment = new MapShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // locationController.queryLatestAddress();
        try {
            mListener = (OnMapFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        app = (TaskManagerApplication) this.getActivity().getApplication();
        app.getLocationController().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView= inflater.inflate(R.layout.fragment_map, container, false);

        mAddress = (TextView) fragmentView.findViewById(R.id.address);
        mActivityIndicator = (ProgressBar) fragmentView.findViewById(R.id.address_progress);

        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.fragment_map_fragment_show)).getMap();

        return fragmentView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // locationController.unregister(this);
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void locationChanged(LocationController locationController)
    {
        // Turn off the progress bar
        mActivityIndicator.setVisibility(View.GONE);

        // Set the address in the UI
        mAddress.setText(locationController.getLatestAddress());

        if (!addressRetrieved)
        {

            Location address = locationController.getLocation();
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latlng));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlng)
                    .zoom(10)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            addressRetrieved=true;
        }


    }

    @Override
    public void locationControllerConnected(LocationController locationController)
    {


        if (locationController.getLatestAddress()==null)
        {
            locationController.queryLatestAddress();
        }
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
    public interface OnMapFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
