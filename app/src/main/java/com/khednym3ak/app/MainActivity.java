package com.khednym3ak.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView mTextView;
    public String username;
    public String email;
    private String userID;

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.home) {
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, homeFragment);
                    fragmentTransaction.commit();
                }
                if (id == R.id.search) {
                    SearchFragment searchFragment = new SearchFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, searchFragment);
                    fragmentTransaction.commit();
                }
                if (id == R.id.offer) {
                    OfferFragment offerFragment = new OfferFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, offerFragment);
                    fragmentTransaction.commit();
                }
                if (id == R.id.profile) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, profileFragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });

        //default fragment
        bottomNavigationView.setSelectedItemId(R.id.home);



        // Initialize Firebase Auth and get User
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "no User found!");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        userID = currentUser.getUid();

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        getUserInfo();
    }




    private void getUserInfo(){
        Query query = mDatabase.child(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if (user !=null){
                    email = user.getEmail();
                    username = user.getUsername();

                } else {
                    Toast.makeText(MainActivity.this, "No User catched from Database.", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, " No Data catched", Toast.LENGTH_LONG).show();            }
        });
    }

}
