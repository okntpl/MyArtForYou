package com.okantopal.mymusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    FeedRecyclerAdapter feedRecyclerAdapter;

    ArrayList<String> userEmailFromFB;
    ArrayList<String> userNameFromFB;
    ArrayList<String> userTitleFromFB;
    ArrayList<String> userVideoFromFB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_feed);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFireStore();

        userTitleFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userVideoFromFB = new ArrayList<>();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFromFB, userTitleFromFB, userVideoFromFB);
        recyclerView.setAdapter(feedRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mymusic_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addpost) {
            Intent intentUpload = new Intent(FeedActivity.this, UploadActivity.class);

            startActivity(intentUpload);
        } else if (item.getItemId() == R.id.signout) {
            firebaseAuth.signOut();

            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromFireStore() {
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");



        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(FeedActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String caption = (String) data.get("caption");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");

                        userEmailFromFB.add(userEmail);
                        userTitleFromFB.add(caption);
                        userVideoFromFB.add(downloadUrl);
                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }
        });


    }
}
