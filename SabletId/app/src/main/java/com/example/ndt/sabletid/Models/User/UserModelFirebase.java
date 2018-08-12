package com.example.ndt.sabletid.Models.User;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserModelFirebase {
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface LoginListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void loginWithEmailAndPassword(String email, String password, final LoginListener callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            callback.onFailure("login has failed: " + task.getException().getMessage());
                        } else {
                            callback.onSuccess();
                        }
                    }
                });
    }
}
