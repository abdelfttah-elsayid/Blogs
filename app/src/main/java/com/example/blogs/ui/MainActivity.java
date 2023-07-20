package com.example.blogs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.blogs.R;
import com.example.blogs.adapters.BlogAdapter;
import com.example.blogs.databinding.ActivityMainBinding;
import com.example.blogs.models.AppUser;
import com.example.blogs.models.Blog;
import com.example.blogs.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<AppUser> appUserList = new ArrayList<>();
    List<Blog> blogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(fcmToken -> {
            Log.i(TAG, "onCreate: " +fcmToken );
        });

        Toolbar toolbar = findViewById(R.id.toolbar); // Reference to the Toolbar from XML
        setSupportActionBar(toolbar); // Set the Toolbar as the app bar

        binding.addNewBlog.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNewBlogActivity.class));
           // throw new RuntimeException("Test Crash"); // Force a crash

        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        getBlogs();

    }

    private void getBlogs() {
        firestore.collection("Blogs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot query) {
                blogList.clear();

                // for each
                for (QueryDocumentSnapshot document : query) {
                    Blog blog = document.toObject(Blog.class);
                    blogList.add(blog);

                }


               /*
                // for i

                for (int i = 0; i <query.size() ; i++) {
                    Blog blog = query.getDocuments().get(i).toObject(Blog.class);
                    blogList.add(blog);

                } */
                getBlogsUsersDataP();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*private void getBlogsUsersData() {
        appUserList.clear();

        for (Blog blog : blogList) {
            firestore.collection("ultras").document(blog.getUserId())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        AppUser appUser = documentSnapshot.toObject(AppUser.class);
                        appUserList.add(appUser);
                    });

        }

        BlogAdapter blogAdapter = new BlogAdapter(userMap, blogList);
        binding.rvBlogs.setAdapter(blogAdapter);
    }*/

    private int usersCounter = 0;
    Map<String , AppUser > userMap = new HashMap<>();

    private void getBlogsUsersDataP() {
        Log.i(TAG, "getBlogsUsersDataP: start");
        if (usersCounter >= blogList.size()){
            Log.i(TAG, "getBlogsUsersDataP: finish");
            BlogAdapter blogAdapter = new BlogAdapter(userMap , blogList);
            binding.rvBlogs.setAdapter(blogAdapter);
            return;
        }

        String userId = blogList.get(usersCounter).getUserId();

        if(userMap.containsKey(userId) ){
            Log.i(TAG, "getBlogsUsersDataP: contain");
            usersCounter++;
            getBlogsUsersDataP();
            return;
        }
        Log.i(TAG, "getBlogsUsersDataP: getUserData");
        firestore.collection("ultras").document(userId)
                .get().addOnSuccessListener(documentSnapshot -> {
                    AppUser appUser = documentSnapshot.toObject(AppUser.class);

                    appUserList.add(documentSnapshot.toObject(AppUser.class));
                    Log.e(TAG, "getBlogsUsersDataP: "+ appUser.toString() );
                    userMap.put(userId , appUser);
                    //appUserList.add(appUser);
                    usersCounter++;
                    Log.i(TAG, "getBlogsUsersDataP: user data added");
                    if(usersCounter < blogList.size()){
                        getBlogsUsersDataP();
                    }
                    else
                    {
                        BlogAdapter blogAdapter = new BlogAdapter(userMap, blogList);
                        binding.rvBlogs.setAdapter(blogAdapter);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(MainActivity.this , LoginActivity.class));
        } else if (item.getItemId() == R.id.item_profile){
            Intent intent = new Intent(this , ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}