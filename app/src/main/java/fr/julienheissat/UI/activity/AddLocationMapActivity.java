package fr.julienheissat.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import fr.julienheissat.taskmanager.R;


public class AddLocationMapActivity extends Activity
{
    private GoogleMap mMap;

    public static final String ADDRESS_RESULT = "address";
    private Button mapLocationButton;
    private EditText addressText;
    private Button useLocationButton;
    private Address address;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_location_map);

            setupViews();
        }





    private void setupViews()
    {

        addressText = (EditText) findViewById(R.id.task_address);
        mapLocationButton = (Button) findViewById(R.id.map_location_button);
        useLocationButton = (Button) findViewById(R.id.use_this_location_button);


        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        useLocationButton.setEnabled(false);

        mapLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mapCurrentAddress();
            }
        });

        useLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (null!=address)
                {
                    Intent intent=new Intent();
                    intent.putExtra(ADDRESS_RESULT,address);
                    setResult(RESULT_OK,intent);

                }

                finish();
            }
        });


    }

    protected void mapCurrentAddress()
    {
        String addressString = addressText.getText().toString();
        Geocoder g = new Geocoder(this);
        List<Address> addresses;
        try
        {
            addresses= g.getFromLocationName(addressString,1);

            if (null!=addresses && addresses.size()>0)
            {
                address = addresses.get(0);
                LatLng latlng= new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(String.valueOf(addressText)));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(17)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                useLocationButton.setEnabled(true);
            }
            else
            {
                //Tell the user no results for address
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }


    }



}
