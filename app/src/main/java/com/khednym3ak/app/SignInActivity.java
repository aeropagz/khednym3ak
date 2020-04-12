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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = SignInActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    public ProgressBar mProgressBar;

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setProgressBar(R.id.progress_bar);


        //EditText
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);

        //Buttons onClickListner

        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.button_go_to_sign_up).setOnClickListener(this);



        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick (View view) {
        int viewId = view.getId();
        switch (viewId){
            case R.id.button_sign_in:
                Log.d(TAG, "Sign In pressed");
                signIn(mEmailField.getText().toString(),mPasswordField.getText().toString());
                break;
            case R.id.button_go_to_sign_up:
                Log.d(TAG, "Sign Up pressed");
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    private void signIn (String email, String password){
        Log.d(TAG, "signing in: " + email);
        if(!validateForm()){
            return;
        }
        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:success");
                        hideProgressBar();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END sign_in_with_email]
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
