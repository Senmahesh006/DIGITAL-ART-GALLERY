package com.example.art_gallery.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.art_gallery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile_Page extends AppCompatActivity {

    TextView name, email;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String userID;

    private Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        name = findViewById(R.id.fullname);
        email = findViewById(R.id.getEmail);
        button = findViewById(R.id.btnlogout);

        fAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        userID =fAuth.getCurrentUser().getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                signout();
            }
        });

        DocumentReference documentReference=firestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot , @Nullable FirebaseFirestoreException error) {
                name.setText(documentSnapshot.getString("fUser"));
                email.setText(documentSnapshot.getString("femail"));

            }
        });
    }

    private void signout() {

        Intent intent = new Intent(Profile_Page.this,Login_Page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
