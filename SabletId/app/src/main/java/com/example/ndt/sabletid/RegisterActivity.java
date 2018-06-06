package com.example.ndt.sabletid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.tbRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goToNextActivity);
                                }

                                // TODO: Should alert when login failed
                            }
                        });
            }
        });
    }
}
