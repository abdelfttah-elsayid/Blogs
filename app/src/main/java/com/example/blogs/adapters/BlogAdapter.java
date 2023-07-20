package com.example.blogs.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogs.databinding.ItemBlogBinding;
import com.example.blogs.ui.BlogDetailsActivity;
import com.example.blogs.ui.ProfileDetailsActivity;
import com.example.blogs.R;
import com.example.blogs.models.AppUser;
import com.example.blogs.models.Blog;

import java.util.List;
import java.util.Map;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogHolder> {

    List<Blog>blogList;

    Map<String , AppUser > appUserMap;

    public BlogAdapter( Map<String, AppUser> appUserMap ,List<Blog> blogList ) {
        this.appUserMap = appUserMap;
        this.blogList = blogList;

    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlogHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.item_blog , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolder holder, int position) {
        Blog blog = blogList.get(position);

        AppUser appUser = appUserMap.get(blog.getUserId());

        holder.binding.setUser(appUser);
        holder.binding.setBlog(blog);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , BlogDetailsActivity.class);
                intent.putExtra("blog" ,blog );
                intent.putExtra("user" ,appUser );
                v.getContext().startActivity(intent);

            }
        });

        holder.binding.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , ProfileDetailsActivity.class);
                intent.putExtra("user" ,appUser );
                v.getContext().startActivity(intent);
            }
        });
        holder.binding.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , ProfileDetailsActivity.class);
                intent.putExtra("user" ,appUser );
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class BlogHolder extends RecyclerView.ViewHolder{
        ItemBlogBinding binding;

        public BlogHolder(@NonNull ItemBlogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}
