package com.mycomsci.mymarker;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        for(int i=0;i<50;i++){
            String url = "http://media.istockphoto.com/photos/senior-farmer-in-a-field-examining-crop-picture-id514790466?k=6&m=514790466&s=612x612&w=0&h=uQrXM7hwdGY4tfwqttDGBSp6KYnzPK1ukqUG_h93Imc=&v="+i+(new Date().getTime());

            LatLng sydney2 = new LatLng(-34+i, 151+i);
            MarkerOptions op = new MarkerOptions().position(sydney2).title("Marker in Sydney");
            IconMarker.getInstan(this).setMarkerAvatar(mMap.addMarker(op),url);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void setList(){

    }
}
