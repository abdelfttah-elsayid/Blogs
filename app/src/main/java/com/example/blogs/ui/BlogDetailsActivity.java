package com.example.blogs.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.blogs.R;
import com.example.blogs.databinding.ActivityBlogDetailsBinding;
import com.example.blogs.models.AppUser;
import com.example.blogs.models.Blog;

public class BlogDetailsActivity extends AppCompatActivity {
ActivityBlogDetailsBinding binding ;
AppUser user ;
Blog blog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding = DataBindingUtil.setContentView(BlogDetailsActivity.this , R.layout.activity_blog_details);
         blog =  getIntent().getParcelableExtra("blog");
         user=  getIntent().getParcelableExtra("user");
         binding.setBlog(blog);
         binding.setUser(user);

    }
}