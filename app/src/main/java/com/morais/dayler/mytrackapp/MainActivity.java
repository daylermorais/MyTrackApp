package com.morais.dayler.mytrackapp;

import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements GPSCallback {
    private GoogleMap googleMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    private GPSManager gpsManager = null;
    double auxLat, auxLng;
    LatLng auxLatLng;
    float cameraZoom;
    ArrayList<LatLng> points;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        points = new ArrayList<LatLng>();

        gpsManager = new GPSManager();
        gpsManager.startListening(getApplicationContext());
        gpsManager.setGPSCallback(this);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment =  (MapFragment) fragmentManager.findFragmentById(R.id.map);
        googleMap = mapFragment.getMap();

        //LatLng sfLatLng = new LatLng(37.7750, -122.4183);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.addMarker(new MarkerOptions()
        //        .position(sfLatLng)
        //        .title("San Francisco")
        //        .snippet("Population: 776733")
        //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //LatLng sLatLng = new LatLng(37.857236, -122.486916);
        //googleMap.addMarker(new MarkerOptions()
        //        .position(sLatLng)
        //        .title("Sausalito")
        //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));


        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //LatLng cameraLatLng = sfLatLng;
        cameraZoom = 15;

        if(savedInstanceState != null){
            mapType = savedInstanceState.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);

            double savedLat = savedInstanceState.getDouble("lat");
            double savedLng = savedInstanceState.getDouble("lng");
            //cameraLatLng = new LatLng(savedLat, savedLng);

            //cameraZoom = savedInstanceState.getFloat("zoom", 10);
        }

        googleMap.setMapType(mapType);
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));
    }

    @Override
    public void onGPSUpdate(Location location)
    {
        auxLat = location.getLatitude();
        auxLng = location.getLongitude();

        auxLatLng = new LatLng(auxLat, auxLng);
        //googleMap.addMarker(new MarkerOptions()
        //        .position(auxLatLng)
        //        .title("Estou Aqui")
        //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(10);
        points.add(auxLatLng);
        polylineOptions.addAll(points);
        googleMap.addPolyline(polylineOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(auxLatLng, cameraZoom));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.normal_map:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                break;

            case R.id.satellite_map:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;

            case R.id.terrain_map:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;

            case R.id.hybrid_map:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
        }

        googleMap.setMapType(mapType);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the map type so when we change orientation, the mape type can be restored
        LatLng cameraLatLng = googleMap.getCameraPosition().target;
        float cameraZoom = googleMap.getCameraPosition().zoom;
        outState.putInt("map_type", mapType);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }
}