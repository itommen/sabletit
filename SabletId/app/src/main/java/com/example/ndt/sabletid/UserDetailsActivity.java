package com.example.ndt.sabletid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

        findViewById(R.id.btnDeleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(user.getUid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(UserDetailsActivity.this, "Your account has been deleted successfully",
                                                                    Toast.LENGTH_LONG).show();

                                                            Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                                            startActivity(goToNextActivity);
                                                        }
                                                        else {
                                                            Toast.makeText(UserDetailsActivity.this, "Failed to delete user, please try again",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                }
                                else {
                                    Toast.makeText(UserDetailsActivity.this, "Failed to delete user, please try again",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
