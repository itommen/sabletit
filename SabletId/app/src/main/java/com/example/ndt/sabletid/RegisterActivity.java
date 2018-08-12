package com.example.ndt.sabletid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.ViewModels.UserViewModel;

public class RegisterActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText email, password, name, phoneNumber;
    private Boolean gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        findViewById(R.id.registerIndeterminateBar).setVisibility(View.INVISIBLE);
        email = findViewById(R.id.etRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);
        name = findViewById(R.id.etRegisterName);
        phoneNumber = findViewById(R.id.etRegisterPhone);
        gender = false;

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.registerIndeterminateBar).setVisibility(View.VISIBLE);

                String emailText = email.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String phoneNumberText = phoneNumber.getText().toString().trim();

                User user = new User(nameText,
                        emailText,
                        phoneNumberText,
                        gender);

                userViewModel.registerUser(user, emailText, passwordText, new UserViewModel.RegisterListener() {
                    @Override
                    public void onSuccess(User user) {
                        findViewById(R.id.registerIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(RegisterActivity.this, "Your account has been created successfully",
                                Toast.LENGTH_LONG).show();

                        Intent goToNextActivity = new Intent(getApplicationContext(), UserDetailsActivity.class);
                        startActivity(goToNextActivity);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        findViewById(R.id.registerIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(RegisterActivity.this, "Failed to register, please try again",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        findViewById(R.id.btnMoveLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goToNextActivity);
            }
        });
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioRegisterMale:
                if (checked)
                    gender = false;

                    break;
            case R.id.radioRegisterFemale:
                if (checked)
                    gender = true;

                    break;
        }
    }
}
