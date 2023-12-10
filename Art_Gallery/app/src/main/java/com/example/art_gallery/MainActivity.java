package com.example.art_gallery;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.art_gallery.Adapter.mainadapter;
import com.example.art_gallery.Login.Login_Page;
import com.example.art_gallery.Login.Profile_Page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth fAuth;
    private mainadapter adapter;

    private ArrayList<upload_Image_Model> ImageModel;
    private ArrayList<String> imageids = new ArrayList<>();
    RecyclerView recycler;


    private DatabaseReference reference;

    FirebaseFirestore db;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();


        reference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        recycler = findViewById(R.id.recyclerview);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);


        getImage();



    }

    private void getImage() {
        imageids = new ArrayList<>();
        ImageModel = new ArrayList<>();
        CollectionReference collectionReference = db.collection("Image");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                    for (DocumentSnapshot snapshot : snapshots) {
                        try {
                            String data = snapshot.getId();
                            imageids.add(data);
                        } catch (DatabaseException e) {

                            Log.e("FirebaseError", "Error: " + e.getMessage());
                        }

                    }
                    for (String id : imageids) {
                        DocumentReference docRef = db.collection("Image").document(id);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        upload_Image_Model data = document.toObject(upload_Image_Model.class);
                                        ImageModel.add(data);
                                        if (ImageModel.size() > 0) {
                                            adapter = new mainadapter(MainActivity.this, ImageModel);
                                            adapter.notifyDataSetChanged();
                                            recycler.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
                                            recycler.setAdapter(adapter);
                                        }
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, Profile_Page.class);
            startActivity(intent);
        } else if (itemId == R.id.upload) {
            Intent intent = new Intent(MainActivity.this, Gallery_Image_Upload.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}