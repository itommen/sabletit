package com.example.ndt.sabletid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndt.sabletid.ViewModels.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText email, password;
    private String emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        findViewById(R.id.loginIndeterminateBar).setVisibility(View.INVISIBLE);
        email = findViewById(R.id.tbLoginEmail);
        password = findViewById(R.id.etLoginPassword);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loginIndeterminateBar).setVisibility(View.VISIBLE);
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();

                userViewModel.login(emailText, passwordText, new UserViewModel.LoginListener() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.loginIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(LoginActivity.this, "Welcome to SubletIt",
                                Toast.LENGTH_LONG).show();

                        Intent goToNextActivity = new Intent(getApplicationContext(), UserDetailsActivity.class);
                        startActivity(goToNextActivity);
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        findViewById(R.id.loginIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(LoginActivity.this, "Failed to login, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        findViewById(R.id.btnMoveRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(goToNextActivity);
            }
        });
    }
}
