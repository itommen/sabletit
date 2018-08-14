package com.example.ndt.sabletid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.ViewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private FirebaseUser firebaseUser;
    private User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        final EditText etUserName = findViewById(R.id.etUserNameUserDetails);
        final EditText etEmail = findViewById(R.id.etEmailUserDetails);
        final EditText etPhone = findViewById(R.id.etPhoneUserDetails);
        final AppCompatRadioButton rbMale = findViewById(R.id.radioRegisterMale);
        final AppCompatRadioButton rbFemale = findViewById(R.id.radioRegisterFemale);

        userViewModel.getConnectedUser().observe(UserDetailsActivity.this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                connectedUser = user;

                if (user != null) {
                    etUserName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());

                    if (user.getGender()) {
                        rbFemale.setChecked(true);
                    }
                    else {
                        rbMale.setChecked(true);
                    }
                }
            }
        });

        findViewById(R.id.btnDeleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               firebaseUser = userViewModel.getFirebaseUser();
               userViewModel.deleteUser(firebaseUser, connectedUser, new UserViewModel.DeleteUserListener() {
                   @Override
                   public void onSuccess() {
                       Toast.makeText(UserDetailsActivity.this, "Your account has been deleted successfully",
                               Toast.LENGTH_LONG).show();

                       Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                       startActivity(goToNextActivity);
                   }

                   @Override
                   public void onFailure(String errorMessage) {
                       Toast.makeText(UserDetailsActivity.this, "Failed to delete user, please try again",
                               Toast.LENGTH_LONG).show();
                   }
               });
            }
        });
    }
}
