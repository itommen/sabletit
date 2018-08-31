package com.example.ndt.sabletid;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndt.sabletid.ViewModels.UserViewModel;

public class LoginFragment extends Fragment {
    private UserViewModel userViewModel;
    private EditText email, password;
    private String emailText, passwordText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        view.findViewById(R.id.loginIndeterminateBar).setVisibility(View.INVISIBLE);
        email = view.findViewById(R.id.tbLoginEmail);
        password = view.findViewById(R.id.etLoginPassword);

        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.loginIndeterminateBar).setVisibility(View.VISIBLE);
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();

                userViewModel.login(emailText, passwordText, new UserViewModel.LoginListener() {
                    @Override
                    public void onSuccess() {
                        view.findViewById(R.id.loginIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(view.getContext(), "Welcome to SubletIt",
                                Toast.LENGTH_LONG).show();

                        Fragment newFragment = new UserDetailsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        transaction.replace(R.id.content_frame, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }

                    @Override
                    public void onFailure(String exceptionMessage) {
                        view.findViewById(R.id.loginIndeterminateBar).setVisibility(View.GONE);

                        Toast.makeText(view.getContext(), "Failed to login, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        view.findViewById(R.id.btnMoveRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }
}
