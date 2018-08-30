package com.example.ndt.sabletid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.ViewModels.UserViewModel;

public class RegisterActivity extends Fragment {
    private UserViewModel userViewModel;
    private EditText email, password, name, phoneNumber;
    private Boolean gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.activity_register, container, false);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        view.findViewById(R.id.registerIndeterminateBar).setVisibility(View.INVISIBLE);
        email = view.findViewById(R.id.etRegisterEmail);
        password = view.findViewById(R.id.etRegisterPassword);
        name = view.findViewById(R.id.etRegisterName);
        phoneNumber = view.findViewById(R.id.etRegisterPhone);
        gender = false;

        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.registerIndeterminateBar).setVisibility(View.VISIBLE);

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
                        view.findViewById(R.id.registerIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(view.getContext(), "Your account has been created successfully",
                                Toast.LENGTH_LONG).show();

                        Fragment newFragment = new UserDetailsActivity();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.content_frame, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        view.findViewById(R.id.registerIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(view.getContext(), "Failed to register, please try again",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        view.findViewById(R.id.btnMoveLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new LoginActivity();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        view.findViewById(R.id.radioRegisterMale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        view.findViewById(R.id.radioRegisterFemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        return view;
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
