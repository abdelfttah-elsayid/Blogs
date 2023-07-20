package com.example.blogs.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blogs.R;
import com.example.blogs.databinding.ActivityAddNewBlogBinding;
import com.example.blogs.models.Blog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddNewBlogActivity extends AppCompatActivity {
    ActivityAddNewBlogBinding binding;
    Uri imageUri = null;
    String title , content;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewBlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar); // Reference to the Toolbar from XML
        setSupportActionBar(toolbar);
        binding.ivSelectCover.setOnClickListener(v->selectImage());
        binding.addBlog.setOnClickListener(v -> getDataFromUi());


    }

    private void getDataFromUi() {
        // 1 _ getDataFromUi
        // 2_ validation
        // 3_ uploadImage
        // 4_getImageUrl
        // 5_addBlogDataInFirestoreDatabase
        
        
         title= binding.etTitle.getText().toString();
        if (title.isEmpty()){
            binding.etTitle.setError("Title is required");
            return;
        }
         content = binding.etContent.getText().toString();
        if (content.isEmpty()){
            binding.etContent.setError("content required");
            return;
        }
        uploadImage();


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
             imageUri = data.getData();

            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(binding.ivSelectCover);
            } else {
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage() {
        storage.child("profilesPictures").child(auth.getUid()).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    getImageUrl();
                }).addOnFailureListener(e ->
                        Toast.makeText(AddNewBlogActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getImageUrl() {
        storage.child("profilesPictures").child(auth.getUid()).getDownloadUrl()
                .addOnSuccessListener(imageUrl ->createNewBlog(imageUrl.toString()) )
                .addOnFailureListener(e ->
                        Toast.makeText(AddNewBlogActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void createNewBlog(String imageUrl) {
        //userId , BlogId , imageUrl ,title , content
            String userId = auth.getUid();
            String blogId = System.currentTimeMillis()+userId;
            Blog blog = new Blog(userId , blogId , imageUrl , title , content);

            firestore.collection("Blogs").document(blogId).set(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AddNewBlogActivity.this, "Blog Added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewBlogActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

}