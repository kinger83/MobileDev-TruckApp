package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        binding.welcomeUserTextView.setText("Welcome " + userName);
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
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

            }
                else {
                    Toast.makeText(LoggedInActivity.this, "Error accessing user details", Toast.LENGTH_SHORT).show();
                }
        }
                });
    }// end setUserData
}