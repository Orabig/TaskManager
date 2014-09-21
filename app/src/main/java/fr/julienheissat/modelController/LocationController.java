package fr.julienheissat.modelcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.utils.LocationUtils;
import fr.julienheissat.utils.PlayConnectionService;

/**
 * Created by juju on 20/09/2014.
 */
public class LocationController implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener


{
    private final TaskManagerApplication app;
    private ArrayList<LocationControllerListener> listOfListener;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location mLatestLocation;
    private String mLatestAddress;


    public LocationController(TaskManagerApplication app)
    {

        this.app = app;

        listOfListener = new ArrayList<LocationControllerListener>();

        mLocationRequest = LocationRequest.create().setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        mLocationClient = new LocationClient(app, this, this);

        mLocationClient.connect();

    }

    public Location getLocationUpdates()
    {

        if (PlayConnectionService.servicesConnected(app) && mLocationClient.isConnected())
        {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
            mLatestLocation = mLocationClient.getLastLocation();
        }

        return mLatestLocation;
    }

    public Location getLocation()
    {
        if (PlayConnectionService.servicesConnected(app) && mLocationClient.isConnected())
        {
            return mLocationClient.getLastLocation();
        } else
        {
            return null;
        }
    }


    public void stopLocationUpdates()
    {
        if (PlayConnectionService.servicesConnected(app) && mLocationClient.isConnected())
        {
            mLocationClient.removeLocationUpdates(this);
        }
    }


    public void disconnect()
    {
        mLocationClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {

        updateAllListenersOnConnection();

    }

    @Override
    public void onDisconnected()
    {

    }


    @Override
    public void onLocationChanged(Location location)
    {
        updateAllListeners();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {

        } else
        {

        }
    }


    public String getLatestAddress ()
    {
       return mLatestAddress;
    }


    @SuppressLint("NewApi")
    public void queryLatestAddress()
    {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent())
        {
            // No geocoder is present. Issue an error message
            //   Toast.makeText(activity, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (PlayConnectionService.servicesConnected(app))
        {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
//            mActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            new GetAddressTask(app).execute(currentLocation);
        }
    }

    public void updateAllListeners()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationChanged(this);
        }
    }

    public void updateAllListenersOnConnection()
    {
        for (LocationControllerListener listener : listOfListener)
        {
            listener.locationControllerConnected(this);
        }
    }


    public void register(LocationControllerListener listener)

    {
        if (listOfListener.size() == 0)
        {
            getLocationUpdates();
        }

        listOfListener.add(listener);
    }

    public void unregister(LocationControllerListener listener)
    {
        listOfListener.remove(listener);
    }

    public void unRegisterAll()
    {
        listOfListener.removeAll(listOfListener);
    }


    public static interface LocationControllerListener
    {
        public void locationChanged(LocationController locationController);

        public void locationControllerConnected(LocationController locationController);
    }

    protected class GetAddressTask extends AsyncTask<Location, Void, String>
    {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context)
        {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params)
        {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
           
                Location location = params[0];

            // Create a list to contain the result address
            List<Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try
            {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
            } catch (IOException exception1)
            {

                // Log an error and return an error message
                Log.e(LocationUtils.APPTAG, app.getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (app.getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2)
            {

                // Construct a message containing the invalid arguments
                String errorString = app.getString(
                        R.string.illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );
                // Log the error and print the stack trace
                Log.e(LocationUtils.APPTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }


            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0)
            {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = app.getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );


                // Return the text
                return addressText;

                // If there aren't any addresses, post a message
            } else
            {
                return app.getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String addressFound)
        {

            mLatestAddress = addressFound;
            updateAllListeners();

        }
    }

}
