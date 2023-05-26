package com.example.truckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.truckapp.databinding.ActivityEstimateBinding;

public class EstimateActivity extends AppCompatActivity {
    ActivityEstimateBinding binding;
    Bundle bundle;
    OwnerModel order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstimateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bundle = getIntent().getBundleExtra("job");
        order = (OwnerModel) bundle.getSerializable("order");


    }
}