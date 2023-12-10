package com.example.art_gallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.art_gallery.Login.Login_Page;
import com.example.art_gallery.Login.Profile_Page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Gallery_Image_Upload extends AppCompatActivity {


    ImageView imageView;

    ImageButton imageButton;
    private Bitmap bitmap;
    private ProgressDialog pd;

    private final int Req = 1;


private Uri img;
    FirebaseStorage storage;

    FirebaseFirestore firestore;

    Toolbar toolbar;
    private StorageReference mstoragref;

    Button Upload;
    String DowlonadUrl = "";
    private  String image,curretntUser,DocId;
    FirebaseAuth fAuth;

    EditText destext,des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_image_upload);

        imageView = findViewById(R.id.prewive);
        Upload = findViewById(R.id.uploadnoticebtn);
        destext = findViewById(R.id.noticetitle);
        imageButton = findViewById(R.id.addimage);
        des = findViewById(R.id.description);
        fAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mstoragref = storage.getReference();

        curretntUser = fAuth.getCurrentUser().getUid();



            getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destext.getText().toString().isEmpty()) {
                    destext.setError("Empty");
                    destext.requestFocus();
                } else if (des.getText().toString().isEmpty()) {
                    des.setError("Empty");
                    des.requestFocus();
                } else {
                    UploadImage();
                }
            }
        });

    }





    private void openGallery() {

        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }
    ActivityResultLauncher<Intent>launcher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result-> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {

                        img = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), img
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (img!=null){
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
    );

    private void UploadImage(){

        pd.setMessage("uploading...");
        pd.show();
        if (img != null){
            final StorageReference  myRef= mstoragref.child("image/"+img.getLastPathSegment());
            myRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if (uri!= null){
                                image=uri.toString();
                                UploadData();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(Gallery_Image_Upload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Gallery_Image_Upload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void UploadData(){

        String text =destext.getText().toString().trim();
        String description =des.getText().toString().trim();

        if (TextUtils.isEmpty(text)&&TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please Fill The Value", Toast.LENGTH_SHORT).show();
        }
        else {
            DocumentReference documentReference = firestore.collection("Image").document();
            upload_Image_Model uploadImageModel = new upload_Image_Model(text,image,curretntUser,DocId,description);

            documentReference.set(uploadImageModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pd.dismiss();
                    if (task.isSuccessful()){
                        if (task.isSuccessful()){
                            DocId=documentReference.getId();
                            uploadImageModel.setDocId(DocId);

                            documentReference.set(uploadImageModel,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "uploaded Successfuly", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(Gallery_Image_Upload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

//    @Override
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
//            Intent intent = new Intent(Gallery_Image_Upload.this, MainActivity.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }


}