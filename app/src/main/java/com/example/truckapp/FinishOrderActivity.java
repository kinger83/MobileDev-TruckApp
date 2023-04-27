package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.truckapp.databinding.ActivityFinishOrderBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FinishOrderActivity extends AppCompatActivity {
    ActivityFinishOrderBinding binding;
    View view;
    FirebaseAuth mAuth =FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

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
        user = mAuth.getCurrentUser();
        binding.createProgressBar.setVisibility(View.GONE);

        setupInputOnclicks(binding);

        binding.createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight = binding.weightEditText.getText().toString();
                width = binding.widthEditText.getText().toString();
                length = binding.lengthEditText.getText().toString();
                height = binding.heightEditText.getText().toString();
                type = binding.otherGoodsTypeEditText.getText().toString();
                truck = binding.otherTruckEditText.getText().toString();

               if(!validateInput(weight, width, length, height, type, truck)){
                   return;
               }

               if(user == null){
                   Toast.makeText(FinishOrderActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                   return;
               }

                binding.createProgressBar.setVisibility(View.VISIBLE);
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("name", name);
                userMap.put("date", date);
                userMap.put("time", time);
                userMap.put("address", address);
                userMap.put("type", type);
                userMap.put("weight", weight);
                userMap.put("length", length);
                userMap.put("width", width);
                userMap.put("height", height);
                userMap.put("truck", truck);
                userMap.put("owner", user.getUid());

                db.collection("orders").document()
                        .set(userMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                binding.createProgressBar.setVisibility(View.GONE);
                                Toast.makeText(FinishOrderActivity.this, "Order Created", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FinishOrderActivity.this, LoggedInActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.createProgressBar.setVisibility(View.GONE);
                                Toast.makeText(FinishOrderActivity.this, "Error creating order", Toast.LENGTH_SHORT).show();
                                Log.w("Error:", "Error adding document", e);
                            }
                        });


            }
        });
    }


    private void setupInputOnclicks(ActivityFinishOrderBinding binding){
        binding.furnitiureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Furniture";
                binding.otherGoodsTypeEditText.setText(type);
            }
        });

        binding.dryGoodsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Dry Goods";
                binding.otherGoodsTypeEditText.setText(type);
            }
        });

        binding.foodText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Food";
                binding.otherGoodsTypeEditText.setText(type);
            }
        });

        binding.buildingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Building Materials";
                binding.otherGoodsTypeEditText.setText(type);
            }
        });

        binding.truckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truck = "Truck";
                binding.otherTruckEditText.setText(truck);
            }
        });

        binding.vanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truck = "Van";
                binding.otherTruckEditText.setText(truck);
            }
        });

        binding.refrigeratedTruckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truck = "Refrigerated Truck";
                binding.otherTruckEditText.setText(truck);
            }
        });

        binding.refrigeratedVanText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truck = "Refrigerated Van";
                binding.otherTruckEditText.setText(truck);
            }
        });

        binding.miniTruckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truck = "Mini-Truck";
                binding.otherTruckEditText.setText(truck);
            }
        });

    }
    private boolean validateInput(String width, String weight, String height, String lenght, String type, String truck){
        if(TextUtils.isEmpty(width)){
            Toast.makeText(this, "Enter a width", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(weight)){
            Toast.makeText(this, "Enter a weight", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(height)){
            Toast.makeText(this, "Enter a height", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(lenght)){
            Toast.makeText(this, "Enter a length", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(type)){
            Toast.makeText(this, "Enter a goods type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(truck)){
            Toast.makeText(this, "Enter a requested vehicle type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}