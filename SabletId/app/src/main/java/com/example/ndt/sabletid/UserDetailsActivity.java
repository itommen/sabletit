package com.example.ndt.sabletid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.EditText;

import com.example.ndt.sabletid.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        final EditText etUserName = (EditText)findViewById(R.id.etUserNameUserDetails);
        final EditText etEmail = (EditText)findViewById(R.id.etEmailUserDetails);
        final EditText etPhone = (EditText)findViewById(R.id.etPhoneUserDetails);
        final AppCompatRadioButton rbMale = (AppCompatRadioButton)findViewById(R.id.radioRegisterMale);
        final AppCompatRadioButton rbFemale = (AppCompatRadioButton)findViewById(R.id.radioRegisterFemale);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setId(dataSnapshot.getKey());
                    etUserName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());

                    if (user.getGender()) {
                        rbFemale.setChecked(true);
                    }
                    else {
                        rbMale.setChecked(false);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
