package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.truckapp.databinding.ActivityLoggedInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.Map;

public class LoggedInActivity extends AppCompatActivity {
    ActivityLoggedInBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        setUserData(user);
        setTruckFragment();

        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(LoggedInActivity.this, binding.menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Log Out")){
                            mAuth.signOut();
                            Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        if (item.getTitle().equals("Home")){
                            Intent intent = new Intent(getApplicationContext(), LoggedInActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        if(item.getTitle().equals("Account")){
                            Toast.makeText(LoggedInActivity.this, "Not yet Implemented", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        if(item.getTitle().equals("My Orders")){
                            setOrderFragment();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        binding.addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateOrderActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

    }


    private void setUserData(FirebaseUser user){
        docRef = db.collection("users").document(mAuth.getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Map doc = document.getData();
                    userName = doc.get("name").toString();
                    binding.welcomeUserTextView.setText("Welcome " + userName);

            }
                else {
                    Toast.makeText(LoggedInActivity.this, "Error accessing user details", Toast.LENGTH_SHORT).show();
                }
        }
                });
    }// end setUserData
    private void setTruckFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrameLayout, new TruckDisplayFragment());
        ft.commit();
    }

    private void setOrderFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentFrameLayout, new OrderDisplayFragment());
        ft.commit();
    }
}