package com.example.art_gallery.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.art_gallery.MainActivity;
import com.example.art_gallery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Page extends AppCompatActivity {

    Button signup,login;
    EditText Rpassword,Remail;
    TextView forget;

    public static final String SHARED_PREFS ="sharedPrefs";
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_page);

        signup=findViewById(R.id.signup);
        Rpassword=findViewById(R.id.password);
        Remail=findViewById(R.id.email);
        login=findViewById(R.id.login);
        forget=findViewById(R.id.forget);
        fAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this,ForgetActivity.class);
                startActivity(intent);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Remail.getText().toString().trim();
                String password = Rpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Remail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Rpassword.setError("Password Is Required");
                    return;
                }
                if (password.length()<8){
                    Rpassword.setError("Password mast be >=8 Characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login_Page.this,"Login Succcessfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(Login_Page.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Page.this,SignUp_Page.class);
                startActivity(intent);

            }
        });
    }

}