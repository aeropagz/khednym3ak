package com.khednym3ak.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        mTextView = findViewById(R.id.sample_text);

        //Buttons
        findViewById(R.id.logout_button).setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        getUserInfo();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //checking if User is signed in.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Log.e(TAG, "no User found!");
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }


    }

    @Override
    protected  void onRestart(){
        super.onRestart();
        getUserInfo();
    }
    protected void onResume(){
        super.onResume();
        getUserInfo();
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.logout_button){
            try{
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            } catch (Exception e){
                Log.e(TAG, "Failed to Logout");
            }
        }
    }

    private void getUserInfo(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);
                    Log.d(TAG,"User: " + user.toString() + " Username: " + user.getUsername());
                    email = user.getEmail();
                    Log.d(TAG, "email 2. " + email);
                    username = user.getUsername();
                    mTextView.setText("Hello " + username + "\nEmail: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, " No Data catched", Toast.LENGTH_LONG).show();            }
        });
    }

}
