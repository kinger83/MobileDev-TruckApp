package com.example.truckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        super.onCreate(savedInstanceState);
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup OnClickListeners

        binding.attemptSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.userNameEditText.getText().toString();
                String email = binding.emailEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                String conPassword = binding.confirmPasswordEditText.getText().toString();

                if( checkFields(name, email, password, conPassword) == false){
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
    }// end onCreate

    private boolean checkFields(String name, String email, String password, String conPassword){
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

        if(!password.equals(conPassword)){
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}