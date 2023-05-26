package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.truckapp.databinding.ActivityCreateOrderBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class CreateOrderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    View view;
    ActivityCreateOrderBinding binding;
    String recieverName;
    String pickUpTime;
    String pickUpAddress, pickupLat,pickupLong;
    String deliverAddress, deliverLat, deliverLong;
    String pickUpDate;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCreateOrderBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        binding.recieverNameEditText.setText(userName);
        Places.initialize(getApplicationContext(), "AIzaSyC0IrZUHCKy4IJH-bNlWqI-IFNIdKD4zvI");
        binding.addressEditText.setFocusable(false);

        binding.addressEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldlist = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldlist).build(getApplicationContext());
                startActivityForResult(intent, 100);
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyC0IrZUHCKy4IJH-bNlWqI-IFNIdKD4zvI");
        binding.createDropOffLocation.setFocusable(false);
        binding.createDropOffLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldlist = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldlist).build(getApplicationContext());
                startActivityForResult(intent, 150);
            }
        });
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String Day = String.valueOf(dayOfMonth);
                String Month = String.valueOf(month) + 1;
                String Year = String.valueOf(year);
                pickUpDate = Day + "/" + Month + "/" + Year;
            }
        });


        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recieverName = binding.recieverNameEditText.getText().toString();
                pickUpTime = binding.pickupTimeEditText.getText().toString();
                pickUpAddress = binding.addressEditText.getText().toString();
                deliverAddress = binding.createDropOffLocation.getText().toString();
                if(!validateInputs()){
                    return;
                }
                Intent intent = new Intent(CreateOrderActivity.this, FinishOrderActivity.class);
                intent.putExtra("name", recieverName);
                intent.putExtra("date", pickUpDate);
                intent.putExtra("time", pickUpTime);
                intent.putExtra("address", pickUpAddress);
                intent.putExtra("pickupLat", pickupLat);
                intent.putExtra("pickupLong", pickupLong);
                intent.putExtra("deliverAddress", deliverAddress);
                intent.putExtra("deliverLat", deliverLat);
                intent.putExtra("deliverLong", deliverLong);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 100 & resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.addressEditText.setText(place.getAddress());
            pickupLat = String.valueOf(place.getLatLng().latitude);
            pickupLong = String.valueOf(place.getLatLng().longitude);
        } else if (requestCode == 150 & resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.createDropOffLocation.setText(place.getAddress());
            deliverLat = String.valueOf(place.getLatLng().latitude);
            deliverLong = String.valueOf(place.getLatLng().longitude);

        } else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Toast.makeText(this, "Error getting location", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateInputs(){
        if(TextUtils.isEmpty(recieverName)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(pickUpDate)){
            Toast.makeText(this, "Select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(pickUpAddress)){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(pickUpTime)){
            Toast.makeText(this, "Enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(deliverAddress)){
            Toast.makeText(this, "Enter delivery address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}