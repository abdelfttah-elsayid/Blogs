package com.example.blogs.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogs.databinding.ItemBlogBinding;
import com.example.blogs.ui.ProfileDetailsActivity;
import com.example.blogs.R;
import com.example.blogs.models.AppUser;
import com.example.blogs.models.Blog;

import java.util.List;
import java.util.Map;

public class ProfileAdapter extends RecyclerView.Adapter <ProfileAdapter.ProfileHolder>{
    List<Blog> blogList;
    Map<String , AppUser > appUserMap;



    public ProfileAdapter(List<Blog> blogList) {
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileAdapter.ProfileHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.item_profile , parent , false));    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        Blog blog = blogList.get(position);
        AppUser appUser = appUserMap.get(blog.getUserId());

        holder.binding.setBlog(blog);

        holder.binding.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , ProfileDetailsActivity.class);
                intent.putExtra("user" ,appUser );
                intent.putExtra("blog" ,blog );

                v.getContext().startActivity(intent);
            }
        });
        holder.binding.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , ProfileDetailsActivity.class);
                intent.putExtra("user" ,appUser );
                intent.putExtra("blog" ,blog );

                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class ProfileHolder extends RecyclerView.ViewHolder{
        ItemBlogBinding binding;

        public ProfileHolder(@NonNull ItemBlogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}