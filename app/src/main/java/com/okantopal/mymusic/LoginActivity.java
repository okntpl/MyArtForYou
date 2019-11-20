package com.okantopal.mymusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailText,passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            Intent intent = new Intent(LoginActivity.this,FeedActivity.class);
        startActivity(intent);
        finish();
        }

    }

    public void signInClicked(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.matches("")) {
            Toast.makeText(LoginActivity.this, "Please enter your mail address", Toast.LENGTH_LONG).show();
        } else if (password.matches("")) {
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public  void signUpClicked2(View view)
    {

        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
         startActivity(intent);
    }

}
