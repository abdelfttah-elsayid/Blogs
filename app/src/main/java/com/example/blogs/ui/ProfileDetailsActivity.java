package com.example.blogs.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blogs.R;
import com.example.blogs.adapters.BlogAdapter;
import com.example.blogs.databinding.ActivityProfileDetailsBinding;
import com.example.blogs.models.AppUser;
import com.example.blogs.models.Blog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity {
    AppUser appUser ;
    List<AppUser> appUserList = new ArrayList<>();

    private static final String TAG = "ProfileDetailsActivity";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<Blog> blogList = new ArrayList<>();

    ActivityProfileDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ProfileDetailsActivity.this ,
                R.layout.activity_profile_details);

            appUser = getIntent().getParcelableExtra("user");
            binding.setUser(appUser);
            getBlogs();

    }
    private void getBlogs() {
        firestore.collection("Blogs")
                .whereEqualTo("userId"  , appUser.getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                Toast.makeText(ProfileDetailsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    } private int usersCounter = 0;
    Map<String , AppUser > userMap = new HashMap<>();

    private void getBlogsUsersDataP() {
        Log.i(TAG, "getBlogsUsersDataP: start");
        if (usersCounter >= blogList.size()){
            Log.i(TAG, "getBlogsUsersDataP: finish");
            BlogAdapter blogAdapter = new BlogAdapter(userMap , blogList);
            binding.rvProfile.setAdapter(blogAdapter);
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
                        binding.rvProfile.setAdapter(blogAdapter);
                    }
                });
    }
}