package com.example.blogs.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.blogs.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
ActivityRegisterBinding binding ;
FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(view -> getDataFromUi());
    }

    private void getDataFromUi() {
        String email = binding.etEmail.getText().toString();
        if (email.isEmpty()){
            binding.etEmail.setError("Email required");
            return;
        }
        String password= binding.etPassword.getText().toString();
        if (password.isEmpty()){
            binding.etPassword.setError("Password required");
            return;
        }
        String confirmPassword= binding.etConfirmPassword.getText().toString();
        if (confirmPassword.isEmpty()){
            binding.etConfirmPassword.setError("Password required");
            return;
        }
        if (password.equals(confirmPassword)){
            createNewAccount(email , password);
        }
        else {
            Toast.makeText(this, "password isn't matching confirm password", Toast.LENGTH_SHORT).show();
        }



    }

    private void createNewAccount(String email , String password) {

        firebaseAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(authResult -> {
            Toast.makeText(RegisterActivity.this, "Email created", Toast.LENGTH_SHORT).show();

            finish();
        }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

    }



}