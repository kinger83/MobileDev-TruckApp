package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.truckapp.databinding.ActivityMainBinding;
import com.example.truckapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        binding.imageView.setImageResource(com.google.firebase.database.collection.R.drawable.common_google_signin_btn_icon_light_normal_background);
        // Setup OnClickListeners
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkFilePermissions();
                Toast.makeText(SignupActivity.this, "Add image here", Toast.LENGTH_SHORT).show();
            }
        });

        binding.attemptSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.userNameEditText.getText().toString();
                String email = binding.emailEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                String conPassword = binding.confirmPasswordEditText.getText().toString();
                String phoneNumber = binding.phoneEditText.getText().toString();

                if( checkFields(name, email, password, conPassword, phoneNumber) == false){
                    return;
                }
                binding.progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    user = mAuth.getCurrentUser();
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("email", email);
                                    userMap.put("phone", phoneNumber);
                                    userMap.put("userID", user.getUid());

                                    db.collection("users")
                                            .add(userMap)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(SignupActivity.this, "Authentication Success.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignupActivity.this, MainDisplay.class);
                                                    intent.putExtra("user", user);
                                                    startActivity(intent);
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Error:", "Error adding document", e);
                                        }
                                    });




                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });

        binding.backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }// end onCreate

    private boolean checkFields(String name, String email, String password, String conPassword, String phone){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(SignupActivity.this, "Enter password ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(SignupActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(conPassword)){
            Toast.makeText(SignupActivity.this, "Enter confirm email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(String.valueOf(phone))){
            Toast.makeText(SignupActivity.this, "Enter confirm phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(conPassword)){
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }




}