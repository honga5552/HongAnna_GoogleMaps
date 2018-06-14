package com.example.anna.mymapsappp1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    private EditText locationSearch;
    private LocationManager locationManager;
    private Location myLocation;
    private boolean gotMyLocationOneTime;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private double latitude, longitude;
    private double previousLatitude, previousLongitude;
    private int trackMarkerDropCounter = 0;
    private boolean notTrackingMyLocation = true;

    // private LocationManager locationManager;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0.0f;
    private static final int MY_LOCATION_ZOOM_FACTOR = 17;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = (Button) findViewById(R.id.button_view);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        //Add a marker at your place of birth and move the camera to it
        //When the marker is tapped, display "Born Here"

        LatLng sanJose = new LatLng(37, -122);
        mMap.addMarker(new MarkerOptions().position(sanJose).title("Born Here")); //FIX THESE COORDINATES lol
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sanJose));

        //To put a dot at where you are currently located - PERMISSION CHECK for location services
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyMapsApp", "Failed FINE Permission Check");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MyMapsApp", "Failed COARSE Permission Check");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        //==? or !=?
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }*/

        locationSearch = (EditText) findViewById(R.id.editText_addr);

        gotMyLocationOneTime = false;
        getLocation();
    }

    //CLEAR BUTTON
    public void clearAll(View view){
        //keep it from clearing birth marker
        Log.d("myMapsApp", "in clear all method");
        mMap.clear();
    }


    //Add a View button and method to switch between the satallite and terrestrial view
    //ASSIGNMENT


    public void dropAmarker(String provider) {

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        myLocation = locationManager.getLastKnownLocation(provider);
        LatLng userLocation = null;
        if(myLocation==null){
            Log.d("MyMaps", "dropAmarker: myLocation is null");
        Toast.makeText(MapsActivity.this, "dropAmarker: location is not null", Toast.LENGTH_SHORT).show();}

        else{
            Log.d("MyMaps", "dropAmarker: location is not null");
            Toast.makeText(MapsActivity.this, "dropAmarker: location is not null", Toast.LENGTH_SHORT).show();

            userLocation= new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation,MY_LOCATION_ZOOM_FACTOR);
            if(provider == LocationManager.GPS_PROVIDER){
                Log.d("MyMaps", "dropAmarker: creatig red circle");
                mMap.addCircle(new CircleOptions()
                        .center(userLocation)
                        .radius(1)
                        .strokeColor(Color.RED)
                        .strokeWidth(2)
                        .fillColor(Color.RED));
            }
            if(provider == LocationManager.NETWORK_PROVIDER){
                Log.d("MyMaps", "dropAmarker: creating blue circle");
                mMap.addCircle(new CircleOptions()
                    .center(userLocation)
                    .radius(1)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(2)
                    .fillColor(Color.BLUE));
            }
            mMap.animateCamera(update);
        }





    //DONE
        //if(locationManager != null)
        //  if (checkSelfPermission fails)
        //      return
        //  myLocation = locationManager.getLastKnownLocation(provider)
        //LatLng userLocation = null;
        //if(myLocation == null) print log or toast message
        //else
        //  userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude
        //  CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation,MY_LOC_ZOOM_FACTORY
        //  if (provider == LocationManager.GPS_PROVIDER)
        //      add circle for the marker with two outer rings
        //      mMap.addCircle(new CircleOptions()
        //          .center(UserLocation)
        //          .radius(1)
        //          .strokeColor(Color.RED)
        //          .strokeWidth(2)
        //          .fillColor(Color.RED)
        //  else add circle for the marker with 2 outer rings (blue)
        // mMap.animateCamera(update)

    }

    public void trackMyLocation(View view) {
        //getLocation();
        if(notTrackingMyLocation) {
            getLocation();
            notTrackingMyLocation = false;
            Toast.makeText(this,"location tracking ON",Toast.LENGTH_SHORT).show();
        }
        else {
            locationManager.removeUpdates(locationListenerGPS);
            locationManager.removeUpdates(locationListenerNetwork);
            notTrackingMyLocation = true;
            Toast.makeText(this,"location tracking OFF",Toast.LENGTH_SHORT).show();
        }

        //kick off the location tracker using getLocation to start the LocationListener
        //if(notTrackingMyLocation) getLocation(); notTrackingMyLocation = false;
        //else (removeUpdates for both network and gps; notTrackingMyLocation = true;
    }

    public void changeView(View view) {
        //  button.setOnClickListener();
        if (mMap.getMapType() == 1)
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        else
            mMap.setMapType(mMap.MAP_TYPE_NORMAL);
    }


    // private LocationManager locationManager;
    //Put a search button. Ex: nearest starbucks
    public void onSearch(View v) {
        //to accesss text
        String location = locationSearch.getText().toString();

        List<Address> addressList = null;
        List<Address> addressListZip = null;

        //Use LocationManager class for user location
        // Implement the LocationListener interface to set up location services
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);

        Log.d("MyMapsApp", "onSearch: location = " + location);
        Log.d("MyMapsApp", "onSearch: provider " + provider);

        LatLng userLocation = null;

        //Check the last known location, need to specifically list the provider (network or gps)

        try {

            if (locationManager != null) {
                Log.d("MyMapsApp", "onSearch: locationManager is not null");
                //get last known location:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if ((myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)) != null) {
                    userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    Log.d("MyMapsApp", "onSearch: using the NETWORK PROVIDER. userLocation is: " +
                            myLocation.getLatitude() + " " + myLocation.getLongitude());
                    Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + " " + myLocation.getLongitude(), Toast.LENGTH_SHORT);
                } else if ((myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)) != null) {
                    userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    Log.d("MyMapsApp", "onSearch: using the NETWORK PROVIDER. userLocation is: " +
                            myLocation.getLatitude() + " " + myLocation.getLongitude());
                    Toast.makeText(this, "UserLoc" + myLocation.getLatitude() + " " + myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("MyMapsApp", "onSearch: myLocation is null from getLastKnownLocation with Network provider");
                }
            } else {
                Log.d("MyMapsApp", "onSearch: myLocation is null!!!");
            }

        } catch (SecurityException | IllegalArgumentException ex) {
            Log.d("MyMapsApp", "onSearch: Exception getLastKNownLocation");
            Toast.makeText(this, "Exception getLastKnownLocation", Toast.LENGTH_SHORT).show();
        }

        if (!location.matches("")) {
            Log.d("MyMapsApp", " onSearch: location field is populated");

            Geocoder geocoder = new Geocoder(this, Locale.US);
            Log.d("MyMapsApp", " onSearch: created Geocoder");

            try {
                //Get a list of  the Addresses
                addressList = geocoder.getFromLocationName(location, 100,
                        userLocation.latitude - (5.0 / 60),
                        userLocation.longitude - (5.0 / 60),
                        userLocation.latitude + (5.0 / 60),
                        userLocation.longitude + (5.0 / 60));

                Log.d("MyMapsApp", " onSearch: addressList is Created");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!addressList.isEmpty()) {
                Log.d("MyMapsApp", "onSearch: AddressList size is " + addressList.size());
                for (int i = 0; i < addressList.size(); i++) {
                    Address address = addressList.get(i);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    //PLACE A MARKER ON THE MAP
                     mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(i)
                            + address.getSubThoroughfare()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                }
            }
        }


    }

    public void getLocation() {
        //Set up try-catch structure with exception for locationManager
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //gettingGPS status. isProviderEnabled ret urns true if user has enabled gps
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) Log.d("MyMapsApp", "getLocation: GPS is enabled");


            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) Log.d("MyMapsApp", "getLocation: Network is enabled");


            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("MyMaps", "getLocation: No provider is enabled");
            } else {
                if (isNetworkEnabled) {
                    Log.d("MyMaps", "getLocation: NetworkEnabled - requesting location updates");


                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    Log.d("MyMaps", "getLocation:NetworkLoc update request successful");
                }

                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);
            }

        } catch (Exception e) {
            Log.d("MyMaps", "Caught exception in getLocation()");
            e.printStackTrace();
        }
    }

    LocationListener locationListenerNetwork = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {
            Log.d("MyMaps", "locationListenerNetwork - onLocationChanged  dropping marker");
            Toast.makeText(MapsActivity.this, "locationListenerNetwork - onLocationChanged  dropping marker", Toast.LENGTH_SHORT).show();

            dropAmarker(LocationManager.NETWORK_PROVIDER);

            if (gotMyLocationOneTime == false) {
                locationManager.removeUpdates(this);
                locationManager.removeUpdates(locationListenerGPS);
                gotMyLocationOneTime = true;

                previousLatitude = latitude;
                previousLongitude = longitude;
            } else {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                //calculate distance??

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("MyMaps", "locationListenerNetwork: onStatusChanged callback");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    LocationListener locationListenerGPS = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d("MyMaps", "locationListenerGPS - onLocationChanged  dropping marker");
            Toast.makeText(MapsActivity.this, "locationListenerGPS - onLocationChanged  dropping marker", Toast.LENGTH_SHORT).show();

            dropAmarker(LocationManager.GPS_PROVIDER);
            if(gotMyLocationOneTime == true){
                locationManager.removeUpdates(locationListenerGPS);
                locationManager.removeUpdates(locationListenerNetwork);
            }
            //done: if doing one time,  remove update to gps and network (?)
            // done:else do nothing
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch(status) {

                case LocationProvider.AVAILABLE: Log.d("MyMapsApp", "locationListenerGPS: location provider available");
                break;

                case LocationProvider.OUT_OF_SERVICE: Log.d("MyMapsApp", "locationListenerGPS: location provider out of service");
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    if(notTrackingMyLocation) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    }
                break;

                case LocationProvider.TEMPORARILY_UNAVAILABLE: Log.d("MyMapsApp", "locationListenerGPS: location provider Temporary unavailable");
                if(!notTrackingMyLocation){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                }

                break;

                default:Log.d("MyMapsApp", "locationListenerGPS: default");
                        if(!notTrackingMyLocation){
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    }

                    break;

            }

            //done: switch(i)
            //done: case LocationProvider.AVAILABlE
            //printout log.d and, or toast message
            //break
            //case LocationProvider.OUT_OF_SERVICE
            //enable network update
            //break;
            //case LocationProvider.TEMPORARILY_UNAVAILABLE;
            //enable both network and gps
            //break
            //default;
            //enable both network and gps
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
















//

