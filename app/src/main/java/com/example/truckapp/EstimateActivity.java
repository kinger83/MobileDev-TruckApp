package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import java.io.IOException;
import java.sql.Time;
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
    GeoApiContext geoApiContext;
    Double price;

    public static final String clientKey = "AVlcCD7GRqNshjDhF-PNHJt5uCaqcjhZqrQrdDvBkjF9c48Tp-knXXjkTsNV44WMGy2xwjR6N0FYyi-J";
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready,
            // switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            // on below line we are passing a client id.
            .clientId(clientKey);


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
                getFlagLocations();
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to get payment.
                getPayment();
            }
        });

        binding.callNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyC0IrZUHCKy4IJH-bNlWqI-IFNIdKD4zvI")
                .build();


        try {
            setMap(googleMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void getFlagLocations(){
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

    private void setMap(GoogleMap googleMap) throws IOException, InterruptedException, ApiException {
        this.gMap = googleMap;
        if (pickup != null && destination != null) {
            this.gMap.addMarker(pickup);
            this.gMap.addMarker(destination);
            LatLngBounds bounds = calculateLatLngBounds(pickup, destination);
            int padding = 100;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            this.gMap.moveCamera(cameraUpdate);

            // Create a Directions API request
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new com.google.maps.model.LatLng(Double.valueOf(pickupLat), Double.valueOf(pickupLong)))
                    .destination(new com.google.maps.model.LatLng(Double.valueOf(deliveryLat), Double.valueOf(deliveryLong)))
                    .await();
            DirectionsRoute[] routes = result.routes; // Get all the routes
            double totalDistance = 0.0;

            // Loop through each route and calculate the total distance
            for (DirectionsRoute route : routes) {
                totalDistance += route.legs[0].distance.inMeters; // Add the distance of each route
            }
            totalDistance = totalDistance/1000;
            distance = String.format("%.2f km", totalDistance); // Convert meters to kilometers

            // Display the total distance in your UI (e.g., TextView)
            //binding.distanceTextView.setText(distance);

            // Loop through each route and calculate the total time
            long totalTimeSeconds = 0;
            for (DirectionsRoute route : routes) {
                totalTimeSeconds += route.legs[0].duration.inSeconds; // Add the duration of each route
            }

            long hours = totalTimeSeconds / 3600;
            long minutes = (totalTimeSeconds % 3600) / 60;
            time = String.format("%02d:%02d", hours, minutes); // Format the time as HH:mm

            // Display the total time in your UI (e.g., TextView)
            //binding.timeTextView.setText(time);

            // Extract the encoded polyline from the DirectionsResult
            String encodedPolyline = result.routes[0].overviewPolyline.getEncodedPath();

            Log.d("**********Distance", distance);
            Log.d("**********Time", time);


            // Decode the polyline to a list of LatLng points
            List<LatLng> decodedPolyline = PolyUtil.decode(encodedPolyline);

            // Create a PolylineOptions and add the decoded polyline points
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(decodedPolyline)
                    .width(5)
                    .color(Color.BLUE);

            // Add the polyline to the map
            gMap.addPolyline(polylineOptions);

            price = 100 + (totalDistance * 4);
            binding.estimateDetails.setText("Distance: " + distance +"         Time: " + time +"\nPrice: " + price);
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

    private void getPayment(){
        // Getting the amount from editText
        String amount = String.valueOf(price);

        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "AUD", "Transport Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        //paymentTV.setText("Payment " + state + "\n with payment id is " + payID);
                        Toast.makeText(this, "Payment " + state + "\nwith paymend id: " + payID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void makeCall(){
        String phoneNumber = "0477228775"; // Replace with the  phone number from driver

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

   
            startActivity(intent);



    }
}