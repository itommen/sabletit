package com.example.ndt.sabletid.Models.User;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModelFirebase {
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface LoginListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void loginWithEmailAndPassword(String email, String password, final LoginListener listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            listener.onFailure("login has failed: " + task.getException().getMessage());
                        } else {
                            listener.onSuccess();
                        }
                    }
                });
    }

    public interface RegisterListener {
        void onSuccess(User user);

        void onFailure(String errorMessage);
    }

    public void createUserWithEmailAndPassword(final User user, final String email, final String password,
                                               final RegisterListener listener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure("registration has failed: " + task.getException().getMessage());
                }
                else {
                    final FirebaseUser firebaseUser = task.getResult().getUser();

                    addUserDetails(firebaseUser, user, listener);
                }
            }
        });
    }

    private void addUserDetails(final FirebaseUser firebaseUser, final User user, final RegisterListener listener) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

        usersReference.child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure("registration has failed: " + task.getException().getMessage());
                }
                else {
                    user.setId(firebaseUser.getUid());
                    listener.onSuccess(user);
                }
            }
        });
    }
}
