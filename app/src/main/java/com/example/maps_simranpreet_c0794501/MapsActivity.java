package com.example.maps_simranpreet_c0794501;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
    private Marker homeMarker;
    private Marker destMarker;

    Marker A;
    Marker B;
    Marker C;
    Marker D;
    LatLng usrloc;
    LatLng first;
    LatLng second;
    LatLng third;
    LatLng forth;

    float totaldist = 0;

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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnPolygonClickListener(this);


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

       // mMap.setOnMapClickListener(this);
    }

    private String returnAdd(LatLng latLng)
    {
        String address = " ";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addressList != null && addressList.size()>0)
            {
                address = "";

                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + "\n";
                if (addressList.get(0).getLocality()!= null)
                    address += addressList.get(0).getLocality() + " ";
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + " ";
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return address;
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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))
                .snippet("your location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrloc,15));
    }


    private void drawShape() {
        PolygonOptions options = new PolygonOptions().fillColor(0x3500FF00).strokeColor(Color.RED).strokeWidth(4).clickable(true);
        for (int i = 0;i<SIDES;i++)
        {
            options.add(markers.get(i).getPosition());
        }

        poly = mMap.addPolygon(options);
        float results[] = new float[10];
        float results1[] = new float[10];
        float results2[] = new float[10];
        float results3[] = new float[10];


        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, results);
        Location.distanceBetween(second.latitude, second.longitude, third.latitude, third.longitude, results1);
        Location.distanceBetween(third.latitude, third.longitude, forth.latitude, forth.longitude, results2);
        Location.distanceBetween(forth.latitude, forth.longitude, first.latitude, first.longitude, results3);

        totaldist = results[0] + results1[0] + results2[0] + results3[0];
    }


    private void setMarkerD(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City D").icon(BitmapDescriptorFactory.fromResource(R.drawable.d));
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        forth = latLng;
        D = mMap.addMarker(options);
        markers.add(D);
        count += 1;
    }

    private void setMarkerC(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City C").icon(BitmapDescriptorFactory.fromResource(R.drawable.c));
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        third = latLng;
        C = mMap.addMarker(options);
        markers.add(C);
        count += 1;
    }

    private void setMarkerB(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City B").icon(BitmapDescriptorFactory.fromResource(R.drawable.b));
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        second = latLng;
        B = mMap.addMarker(options);
        markers.add(B);
        count += 1;
    }

    private void setMarkerA(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title("City A").icon(BitmapDescriptorFactory.fromResource(R.drawable.a));
        float result[] = new float[10];
        Location.distanceBetween(usrloc.latitude,usrloc.longitude,latLng.latitude,latLng.longitude, result);
        options.snippet("Distance = "+ result[0]);

        first = latLng;
        A = mMap.addMarker(options);
        markers.add(A);
      //  options.draggable(true);
        count += 1;

    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {

        if( count ==1)
        {
            setMarkerA(latLng);
        }
        else if (count == 2)
        {
            setMarkerB(latLng);
        }
        else if(count == 3)
        {
            setMarkerC(latLng);
        }
        else if(count == 4)
        {
            setMarkerD(latLng);
            drawShape();
        }
        else if (count == 5)
        {
            clearMarkers();
        }

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

    @Override
    public void onPolygonClick(Polygon polygon)
    {

        Toast.makeText(this, "Distance of all 4 cities  ="+totaldist, Toast.LENGTH_SHORT).show();

    }
}