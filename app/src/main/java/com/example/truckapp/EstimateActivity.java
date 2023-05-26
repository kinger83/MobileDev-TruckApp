package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.truckapp.databinding.ActivityEstimateBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.android.gms.maps.MapFragment;

import java.io.IOException;
import java.util.List;


public class EstimateActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityEstimateBinding binding;
    Bundle bundle;
    OwnerModel order;
    String pickupAddress, pickupLat, pickupLong, deliveryAddress, deliveryLat, deliveryLong, distance, time;
    LatLng pickupp, destinationn;
    MarkerOptions pickup, destination;
    FrameLayout map;
    GoogleMap gMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstimateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bundle = getIntent().getBundleExtra("job");
        if (bundle != null) {
            order = (OwnerModel) bundle.getSerializable("order");
            if (order != null) {
                setRoute();
            }
        }
        //map = findViewById(R.id.map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyC0IrZUHCKy4IJH-bNlWqI-IFNIdKD4zvI")
                .build();


        setMap(googleMap);

    }

    private void setRoute(){
        pickupAddress = order.getPickupAddress();
        pickupLong = order.getPickupLong();
        pickupLat = order.getPickupLat();
        deliveryAddress = order.getDeliveryAddress();
        deliveryLat = order.getPickupLat();
        deliveryLong = order.getDeliverLong();

        pickupp = new LatLng(Double.valueOf(pickupLat), Double.valueOf(pickupLong));
        destinationn = new LatLng(Double.valueOf(deliveryLat), Double.valueOf(deliveryLong));

        pickup = new MarkerOptions().position(pickupp).title("Pick Up");
        destination = new MarkerOptions().position(destinationn).title("Destination");
    }

    private void setMap(GoogleMap googleMap) {
        this.gMap = googleMap;
        if (pickup != null && destination != null) {
            this.gMap.addMarker(pickup);
            this.gMap.addMarker(destination);
            LatLngBounds bounds = calculateLatLngBounds(pickup, destination);
            int padding = 100;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            this.gMap.moveCamera(cameraUpdate);
        }
    }

    private LatLngBounds calculateLatLngBounds(MarkerOptions marker1, MarkerOptions marker2) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (marker1 != null && marker2 != null) {
            builder.include(marker1.getPosition());
            builder.include(marker2.getPosition());
        }
        return builder.build();
    }
}