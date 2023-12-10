package com.example.art_gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.art_gallery.Login.Profile_Page;
import com.jsibbold.zoomage.ZoomageView;

public class Image_des extends AppCompatActivity {

    private ZoomageView imageView ;

    TextView name,des;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_des);

        imageView = findViewById(R.id.photo_view);
        name = findViewById(R.id.textView5);
        des = findViewById(R.id.textView4);
        upload_Image_Model image = (upload_Image_Model) getIntent().getSerializableExtra("image");



        Glide.with(this).load(image.getImage()).into(imageView);
        name.setText(image.getTitle());
        des.setText(image.getDescription());



    }
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId();
//
//
//        if (itemId == R.id.profile) {
//            Intent intent = new Intent(Image_des.this, Profile_Page.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}