package com.khednym3ak.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    public ProfileFragment() {
        // Required empty public constructor
    }
    
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    TextView profileName;
    TextView profileLocation;
    TextView profileEmail;
    TextView profilePhoneNumber;
    TextView profileRating;
    TextView profileDrives;

    Button buttonLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.ggg, container, false);

        profileName = (TextView)view.findViewById(R.id.profile_name);
        profileLocation = (TextView)view.findViewById(R.id.profile_location);
        profileEmail = (TextView)view.findViewById(R.id.profile_email);
        profilePhoneNumber = (TextView)view.findViewById(R.id.profile_phone);
        profileRating = (TextView)view.findViewById(R.id.profile_rating);
        profileDrives = (TextView)view.findViewById(R.id.profile_drive);

        buttonLogout = (Button)view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id==R.id.button_logout){
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }
    }
}
