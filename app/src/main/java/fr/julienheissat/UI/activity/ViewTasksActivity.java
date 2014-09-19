package fr.julienheissat.ui.activity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fr.julienheissat.application.TaskManagerApplication;
import fr.julienheissat.taskmanager.R;
import fr.julienheissat.ui.adapter.TaskListAdapter;
import fr.julienheissat.utils.LocationUtils;

import static fr.julienheissat.utils.Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST;


public class ViewTasksActivity extends ListActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener

{

    private TaskManagerApplication app;
    private TaskListAdapter adapter;

    private ProgressBar mActivityIndicator;
    private TextView mAddress;


    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    /*
    * Lifecycle activity methods
    */


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();


        app = (TaskManagerApplication) getApplication();
        adapter = new TaskListAdapter(app.getTaskList(), this);
        setListAdapter(adapter);


        mAddress = (TextView) findViewById(R.id.address);
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);

        mLocationRequest = LocationRequest.create().setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        stopUpdates();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.forceReload();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();

    }


    public void startUpdates()
    {

        if (servicesConnected() && mLocationClient.isConnected())
        {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
            mCurrentLocation = mLocationClient.getLastLocation();
        }
    }

    public void stopUpdates()
    {
        if (servicesConnected() && mLocationClient.isConnected())
        {
            mLocationClient.removeLocationUpdates(this);
        }
    }

    /*
    *  GooglePlayServicesClient callback methods (ConnectionCallbacks and failed connection callback)
    */

    @Override
    public void onConnected(Bundle bundle)
    {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        startUpdates();
        getAddress();
    }

    @Override
    public void onDisconnected()
    {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e)
            {
                // Log the error
                e.printStackTrace();
            }
        } else
        {

        }
    }

    /*
    * Method to check if app is connected to Google Play services
    */

    private boolean servicesConnected()
    {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
        {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else
        {
            // Display an error dialog

        }
        return false;
    }

    /*
    *  location listener callback methods
    */


    @Override
    public void onLocationChanged(Location location)


    {
        mCurrentLocation = location;
    }


   /*
    *  Methods to retrieve address from location information (using Geocoder)
    */


    @SuppressLint("NewApi")
    public void getAddress()
    {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent())
        {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected())
        {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            mActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            (new ViewTasksActivity.GetAddressTask(this)).execute(currentLocation);
        }
    }

    @Override
    public void onClick(View view)
    {

        if (view.getId() == R.id.remove_button)
        {
            app.getTaskList().removeCompletedTasks();
        } else if (view.getId() == R.id.add_button)
        {
            Intent intent = new Intent(ViewTasksActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
    }

    private void setUpViews()
    {
        findViewById(R.id.add_button).setOnClickListener(this);

        findViewById(R.id.remove_button).setOnClickListener(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        // adapter.toggleTaskCompleteAtPosition(position);
        // Task t = (Task) adapter.getItem(position);
        // app.saveTask(t);

        app.getTaskList().toggleTask(position);

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
                Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2)
            {

                // Construct a message containing the invalid arguments
                String errorString = getString(
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
                String addressText = getString(R.string.address_output_string,

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
                return getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address)
        {

            // Turn off the progress bar
            mActivityIndicator.setVisibility(View.GONE);

            // Set the address in the UI
            mAddress.setText(address);
        }
    }

}
