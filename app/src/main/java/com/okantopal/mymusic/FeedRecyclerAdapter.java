package com.okantopal.mymusic;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {


    private ArrayList<String> userNameList;
    private ArrayList<String> usercaptionList;
    private ArrayList<String> userImageList;



    public FeedRecyclerAdapter(ArrayList<String> userNameList, ArrayList<String> usercaptionList, ArrayList<String> userImageList) {
        this.userNameList = userNameList;
        this.usercaptionList = usercaptionList;
        this.userImageList = userImageList;

    }





    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, int position) {

        holder.usernameText.setText(userNameList.get(position));
        holder.captionText.setText(usercaptionList.get(position));

        Picasso.get().load(userImageList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return usercaptionList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView usernameText;
        TextView captionText;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
        imageView = itemView.findViewById(R.id.recyclerview_row_imageview);
        usernameText = itemView.findViewById(R.id.recyclerview_row_usermail_text);
        captionText = itemView.findViewById(R.id.recyclerview_row_caption_text);
        }
    }
}
