package com.example.blogs.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blogs.R;
import com.example.blogs.databinding.ActivityProfileBinding;
import com.example.blogs.models.AppUser;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    ActivityProfileBinding binding;
    AppUser appUser;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    ProfileViewModel profileViewModel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
       // getUserData();
        binding.btnUpdate.setOnClickListener(view -> getDataFromUi());
        binding.ivProfile.setOnClickListener(v -> selectImage());
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getUserData().observe(this, appUser -> {
            binding.setData(appUser);
        });
        appUser = new AppUser();

    }
    /*private void getUserData(){
        firestore.collection("ultras").document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        appUser = documentSnapshot.toObject(AppUser.class);
                        Log.i(TAG, "getUserData: "+appUser.toString());
                        binding.setData(appUser);

                       /* Glide.with(ProfileActivity.this)
                                .load(appUser.getImageUrl())
                                .circleCrop()
                                .into(binding.ivProfile);

                    }
                    else {

                        appUser = new AppUser();
                    }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
    @BindingAdapter("glideCircleImage")
    public static void glideCircleImage(ImageView imageView , String imageUrl){
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .circleCrop()
                .into(imageView);
    }
    @BindingAdapter("glideLoadImage")
    public static void glideLoadImage(ImageView imageView , String imageUrl){
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
    }


    private void selectImage() {
        ImagePicker.with(this)
                .crop()                     // Crop image(Optional), Check Customization for more option
                .compress(1024)             // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  // Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            // Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();

            if (uri != null) {
                Glide.with(this).load(uri).circleCrop().into(binding.ivProfile);
                uploadImage(uri);
            } else {
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(Uri imageUri) {
        storage.child("profilesPictures").child(auth.getUid()).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    getImageUrl();
                }).addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getImageUrl() {
        storage.child("profilesPictures").child(auth.getUid()).getDownloadUrl()
                .addOnSuccessListener(uri -> addProfilePictureUrlToDatabase(uri.toString()))
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void addProfilePictureUrlToDatabase(String imageUrl) {
        appUser.setImageUrl(imageUrl);

        firestore.collection("ultras").document(auth.getUid()).set(appUser)
                .addOnSuccessListener(unused ->
                        Toast.makeText(ProfileActivity.this, "Image updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getDataFromUi() {
        String name = binding.etName.getText().toString();
        if (name.isEmpty()) {
            binding.etName.setError("Name is required");
            return;
        }

        String phone = binding.etPhone.getText().toString();
        if (phone.isEmpty()) {
            binding.etPhone.setError("Phone is required");
            return;
        }

        appUser.setUserId(auth.getUid());
        appUser.setEmail(auth.getCurrentUser().getEmail());
        appUser.setUsername(name);
        appUser.setPhone(phone);

        addUserDataInFirestore();
    }

    private void addUserDataInFirestore() {
        firestore.collection("ultras").document(auth.getUid()).set(appUser)
                .addOnSuccessListener(unused ->
                        Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }
}
