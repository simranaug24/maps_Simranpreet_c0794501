package com.example.maps_simranpreet_c0794501;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
    private Marker homeMarker;
    private Marker destMarker;

    Marker A;
    Marker B;
    Marker C;
    Marker D;
    LatLng usrloc;

    ArrayList<Marker> markers = new ArrayList<>();
    Polygon poly ;
    public static final int SIDES = 4;

    int count = 1;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


        mMap.setOnMapLongClickListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                setHomeMarker(location);

            }
        };

        if (!isGrantedPermission())

            requestLocationPermission();
        else
            startUpdateLocation();

        mMap.setOnMapClickListener(this);
    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    private boolean isGrantedPermission()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setHomeMarker(Location location)
    {
         usrloc = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions options = new MarkerOptions().position(usrloc)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("your location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrloc,15));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if( count ==1)
        {
            setMarkerA(latLng);
        }
        if (count == 2)
        {
            setMarkerB(latLng);
        }
        if(count == 3)
        {
            setMarkerC(latLng);
        }
        if(count == 4)
        {
            setMarkerD(latLng);
            drawShape();
        }
    }

    private void drawShape() {
        PolygonOptions options = new PolygonOptions().fillColor(0x3500FF00).strokeColor(Color.RED).strokeWidth(4);
        for (int i = 0;i<SIDES;i++)
        {
            options.add(markers.get(i).getPosition());
        }
        poly = mMap.addPolygon(options);
    }


    private void setMarkerD(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City D");
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        D = mMap.addMarker(options);
        markers.add(D);
        count += 1;
    }

    private void setMarkerC(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City C");
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        C = mMap.addMarker(options);
        markers.add(C);
        count += 1;
    }

    private void setMarkerB(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City B");
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        B = mMap.addMarker(options);
        markers.add(B);
        count += 1;
    }

    private void setMarkerA(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City A");
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        A = mMap.addMarker(options);
        markers.add(A);
        count += 1;

    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        clearMarkers();

    }

    private void clearMarkers() {
        for(int i = 0 ; i< markers.size();i++)
        {
            markers.get(i).remove();
        }
        count = 1;
        markers.clear();
        poly.remove();
        poly = null ;
    }
}