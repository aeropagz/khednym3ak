package com.khednym3ak.app;


import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public ProgressBar mProgressBar;

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setProgressBar(R.id.progress_bar);


        //EditText
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        //Buttons onClickListner

        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.button_back_to_login).setOnClickListener(this);


        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick (View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.button_sign_up:
                createAccount(mEmailField.getText().toString(),mPasswordField.getText().toString());
                break;
            case R.id.button_back_to_login:
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:success");
                        hideProgressBar();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void onAuthSuccess(FirebaseUser currentUser){
        String username = usernameFromEmail(currentUser.getEmail());

        writeNewUser(currentUser.getUid(), username, currentUser.getEmail());
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
    
    private void writeNewUser(String userId, String username, String email){
        User user = new User(username, email);
        
        mDatabase.child(userId).setValue(user);
    }

    private String usernameFromEmail(String email){
        if (email.contains("@")){
            return email.split("@")[0];
        }else {
            return email;
        }
    }
    
    //ProgressBar
    public void setProgressBar(int id){
        mProgressBar = findViewById(id);
    }
    

    public void showProgressBar(){
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    //Validate Form
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


}
