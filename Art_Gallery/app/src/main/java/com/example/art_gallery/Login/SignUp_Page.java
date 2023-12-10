package com.example.art_gallery.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.art_gallery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp_Page extends AppCompatActivity {

    String userID;
    public static final String TAP = "Tap";
    EditText username,Rpassword,Remail;

    Button login,signup;

    FirebaseAuth fAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up_page);

        username=findViewById(R.id.user);
        Rpassword=findViewById(R.id.password);
        Remail=findViewById(R.id.email);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        fAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp_Page.this,Login_Page.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Remail.getText().toString().trim();
                String password = Rpassword.getText().toString().trim();
                String fullUser = username.getText().toString().trim();

                if (TextUtils.isEmpty(fullUser)){
                    username.setError("User Name is Required");
                    return;
                }


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

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUp_Page.this,"User Created",Toast.LENGTH_LONG).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=
                                    firestore.collection("Users").document(userID);
                            Map<String,Object> user  = new HashMap<>();
                            user.put("femail",email);
                            user.put("fUser",fullUser);



                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAP,"onSuccess: User Created"+userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Login_Page.class));

                        }else {
                            Toast.makeText(SignUp_Page.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });



            }
        });

    }
}