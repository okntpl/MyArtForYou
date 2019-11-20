package com.okantopal.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText usernameText, emailText, password1Text, password2Text;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        password1Text = findViewById(R.id.password1Text);
        password2Text = findViewById(R.id.password2Text);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


    }

    public void signUpClicked(View view) {

        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password1 = password1Text.getText().toString();
        String password2 = password2Text.getText().toString();






          if (username.matches("")) {
            Toast.makeText(SignUpActivity.this, "username field cannot be empty", Toast.LENGTH_LONG).show();
        }
           else if (email.matches("")) {
            Toast.makeText(SignUpActivity.this, "email field cannot be empty", Toast.LENGTH_LONG).show();
        }  else if (password1.matches("")) {
            Toast.makeText(SignUpActivity.this, "password field cannot be empty", Toast.LENGTH_LONG).show();
        }  else if (password2.matches("")) {
            Toast.makeText(SignUpActivity.this, "please confirm your password", Toast.LENGTH_LONG).show();
        }  else if (!password1.equals(password2)) {
            Toast.makeText(SignUpActivity.this, "passwords doesn't match", Toast.LENGTH_LONG).show();
        }
        else {

              HashMap<String,Object> postData = new HashMap<>();
              postData.put("userName",username);
              postData.put("userEmail",email);


              firebaseFirestore.collection("Usernames").add(postData);


              firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {
                       Toast.makeText(SignUpActivity.this,"SignUp Successful",Toast.LENGTH_LONG).show();
                      Intent intent = new Intent(SignUpActivity.this,FeedActivity.class);

                      startActivity(intent);
                      finish();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                  }
              });
            }
            }
    }



