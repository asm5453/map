package com.example.adammoyer.assignment_maps_adammoyer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String LOG_MAP = "GOOGLE_MAPS";

    private IntentFilter intentFilter = null;
    private MapFragment mapFragment;
    private Marker currentMapMarker;

    FirebaseDatabase firebaseDatabase = null;
    DatabaseReference databaseReference = null;
    private MapBroadcastReciever mapBroadcastReceiver = null;

    private GoogleMap mMap;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mapBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mapBroadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        intentFilter = new IntentFilter("com.example.adammoyer.assignment_maps_adammoyer.action.NEW_MAP_LOCATION_BROADCAST");
        mapBroadcastReceiver = new MapBroadcastReciever();
    }

    public void firebaseLoadData() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();

                for(Map.Entry<String, Object> entry: values.entrySet()) {
                    Log.d(entry.getKey(), "Value is: " + entry.getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        Intent intent = getIntent();
        MapLocation mapLocation =  (MapLocation) intent.getSerializableExtra(MainActivity.TAG_SELECTED_ITEM);

        mapCameraConfiguration(mapLocation.getLatLng());

        useMapClickListener();
        useMarkerClickListener();
        useMapLongClickListener();
        useInfoWindowClickListener();
        createMarkersFromFirebase();
    }

    private void mapCameraConfiguration(LatLng latLng){

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(4)
                .bearing(0)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.addMarker(createCustomMapMarkersFromLocation(latLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Log.i(LOG_MAP, "googleMap.animateCamera:onFinish is active");
            }

            @Override
            public void onCancel() { Log.i(LOG_MAP, "googleMap.animateCamera:onCancel is active"); }});
    }

    private MarkerOptions createCustomMapMarkersFromLocation(LatLng latLng){

        MarkerOptions markerOptions = new MarkerOptions();
        MapLocation mapLocation = MapLocationHelper.getAddressFromLatLgn(getApplicationContext(), latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        markerOptions.position(latLng)
                .title(mapLocation.getCity())
                .snippet(mapLocation.toString());

        return markerOptions;
    }

    private void useMapClickListener(){

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latltn) {
                Log.i(LOG_MAP, "setOnMapClickListener");
                if(currentMapMarker != null)  currentMapMarker.remove();
                MarkerOptions markerOptions = createCustomMapMarkersFromLocation(latltn);
                currentMapMarker = mMap.addMarker(markerOptions);
            }
        });
    }

    private void useMarkerClickListener(){

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                MapLocation mapLocation = MapLocationHelper.getAddressFromLatLgn(getApplicationContext(), latLng);

                if(mapLocation != null){
                    Log.d("MAP_LOCATION", mapLocation.toString());
                    Toast.makeText(getApplicationContext(), mapLocation.toString(), Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getApplicationContext(), "Location not found.", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void useMapLongClickListener() {
        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(6)
                        .bearing(0)
                        .build();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.animateCamera(cameraUpdate, 3000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        Log.i(LOG_MAP, "googleMap.animateCamera:onFinish is active");
                    }

                    @Override
                    public void onCancel() { Log.i(LOG_MAP, "googleMap.animateCamera:onCancel is active"); }});
            }



        });
    }

    private void useInfoWindowClickListener() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
              @Override
              public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
              }
          });
    }


    public void createMarkersFromFirebase(){

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Get the current location (based on the selected dataset available on firebase
                MapLocation currentLocation = dataSnapshot.getValue(MapLocation.class);

                // Adding a new element from the collection
                MarkerOptions markerOptions = createCustomMapMarkersFromLocation(currentLocation.getLatLng());
                mMap.addMarker(markerOptions);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
