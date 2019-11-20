package com.okantopal.mymusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

ImageView imageView;
EditText captionText;
private FirebaseStorage firebaseStorage;
private StorageReference storageReference;
private FirebaseFirestore firebaseFirestore;
private FirebaseAuth firebaseAuth;
    Uri imagedata;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imageView);

        captionText = findViewById(R.id.captionText);

    }

    public void upload(View view) {

        if (imagedata != null) {

            Toast.makeText(UploadActivity.this,"Uploading..",Toast.LENGTH_LONG).show();


            UUID uuid = UUID.randomUUID();

            final String imageName = "images/"+uuid+".jpg";
            storageReference.child(imageName).putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                         String downloadUrl = uri.toString();
                         FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                         String userEmail = firebaseUser.getEmail();
                         String caption = captionText.getText().toString();

                            HashMap<String,Object> postData = new HashMap<>();

                            postData.put("userEmail",userEmail);
                            postData.put("downloadUrl",downloadUrl);
                            postData.put("caption",caption);
                            postData.put("date", FieldValue.serverTimestamp());



                         firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                             @Override
                             public void onSuccess(DocumentReference documentReference) {

                                 Intent intent = new Intent(UploadActivity.this,FeedActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 startActivity(intent);

                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                             }
                         });


                        }
                    });
                    Toast.makeText(UploadActivity.this,"Upload successful",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(UploadActivity.this,"Please select your image to share",Toast.LENGTH_LONG).show();
        }
    }

    public void selectFile(View view)
    {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
            else{

                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    if(requestCode == 1)
    {
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


       if(requestCode==2 && resultCode== RESULT_OK && data !=null)
       {
             imagedata = data.getData();

           try {
               if(Build.VERSION.SDK_INT>=28)
               {
                   ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imagedata);
                   selectedImage = ImageDecoder.decodeBitmap(source);
               }
               else{
               selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
               imageView.setImageBitmap(selectedImage);
                  }
           }catch (IOException e) {
               e.printStackTrace();
           }


       }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
