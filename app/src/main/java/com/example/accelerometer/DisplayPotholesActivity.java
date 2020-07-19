package com.example.accelerometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.accelerometer.model.PotholeData;
import com.example.accelerometer.model.PotholeLatLong;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class DisplayPotholesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<PotholeLatLong> potholeDataList;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private List<LatLng> latLngList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_potholes);
        potholeDataList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().getRoot().child("potholes");
        latLngList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchPotholeData();
    }




    private void fetchPotholeData() {
        //fetch data from firebase
      ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              Log.d("shreyash","onDataChange");

              for(DataSnapshot potholeSnapshot:dataSnapshot.getChildren()){
                  PotholeLatLong potholeData = potholeSnapshot.getValue(PotholeLatLong.class);
                  potholeDataList.add(potholeData);
                  Log.d("shreyash","Data:"+potholeData);
                  LatLng latLng = new LatLng(potholeData.getLatitude(),potholeData.getLongitude());

                  //add markers
                  /*Marker marker = mMap.addMarker(new MarkerOptions()
                          .position(latLng));*/


                  latLngList.add(new LatLng(latLng.latitude, latLng.longitude));

              }

              //show heatmaps on map
              mProvider = new HeatmapTileProvider.Builder()
                      .data(latLngList)
                      .build();

              mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }




}
