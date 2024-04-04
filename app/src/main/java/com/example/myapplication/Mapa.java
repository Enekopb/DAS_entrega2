package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Agregar marcadores para los gimnasios cercanos en bilbo
        LatLng gimnasio1 = new LatLng(43.26138103585541, -2.9267904155771944);
        mMap.addMarker(new MarkerOptions().position(gimnasio1).title("Gimnasio 1"));

        LatLng gimnasio2 = new LatLng(43.27579624505627, -2.918482500509598);
        mMap.addMarker(new MarkerOptions().position(gimnasio2).title("Gimnasio 2"));

        LatLng gimnasio3 = new LatLng(43.26399529693949, -2.9491794740333135);
        mMap.addMarker(new MarkerOptions().position(gimnasio3).title("Gimnasio 3"));

        LatLng gimnasio4 = new LatLng(43.24579173525677, -2.9367976311997173);
        mMap.addMarker(new MarkerOptions().position(gimnasio4).title("Gimnasio 4"));
        // Mover la cámara al primer marcador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gimnasio1, 15));
    }
}

