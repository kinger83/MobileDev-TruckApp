package com.example.truckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.truckapp.databinding.ActivityFinishOrderBinding;

public class FinishOrderActivity extends AppCompatActivity {
    ActivityFinishOrderBinding binding;
    View view;

    String name, date, time, address, type, weight, length, width, height, truck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFinishOrderBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        address = intent.getStringExtra("address");
    }
}