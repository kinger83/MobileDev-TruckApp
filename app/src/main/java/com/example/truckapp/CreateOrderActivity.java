package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.truckapp.databinding.ActivityCreateOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CreateOrderActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    View view;
    ActivityCreateOrderBinding binding;
    String recieverName;
    String pickUpTime;
    String pickUpAddress;
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
                if(!validateInputs(recieverName, pickUpDate, pickUpAddress, pickUpTime)){
                    return;
                }
                Intent intent = new Intent(CreateOrderActivity.this, FinishOrderActivity.class);
                intent.putExtra("name", recieverName);
                intent.putExtra("date", pickUpDate);
                intent.putExtra("time", pickUpTime);
                intent.putExtra("address", pickUpAddress);
                startActivity(intent);
            }
        });

    }

    private boolean validateInputs(String name, String date, String address, String time){
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(date)){
            Toast.makeText(this, "Select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(time)){
            Toast.makeText(this, "Enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}