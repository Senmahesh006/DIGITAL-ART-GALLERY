package com.example.art_gallery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.art_gallery.Image_des;
import com.example.art_gallery.R;
import com.example.art_gallery.upload_Image_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class mainadapter extends RecyclerView.Adapter<mainadapter.ViewHolder> {

    private Context context;
    private ArrayList<upload_Image_Model> uploadImageModels;

    public mainadapter(Context context, ArrayList<upload_Image_Model> uploadImageModels) {
        this.context = context;
        this.uploadImageModels = uploadImageModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        upload_Image_Model currentItem = uploadImageModels.get(position);
        upload_Image_Model uploadModel =uploadImageModels.get(position);

        holder.name.setText(uploadModel.getTitle().toString());
        try {
            if (currentItem.getImage() != null)
                Glide.with(context).load(currentItem.getImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Image_des.class);
                intent.putExtra("image",currentItem);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return uploadImageModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.imgtittle);
            imageView = itemView.findViewById(R.id.gallery_image);
        }
    }
}
