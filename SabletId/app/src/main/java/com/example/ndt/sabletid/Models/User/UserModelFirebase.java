package com.example.ndt.sabletid.Models.User;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserModelFirebase {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface GetUserByIdListener {
        void onSuccess(User user);
    }

    public void getUserById(final String id, final GetUserByIdListener listener) {
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    user.setId(id);
                    listener.onSuccess(user);
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        ref.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public interface DeleteUserListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void deleteUser(final FirebaseUser firebaseUser, final DeleteUserListener listener) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure("delete user has failed: " + task.getException().getMessage());
                }
                else {
                    deleteUserDetails(firebaseUser.getUid(), listener);
                }
            }
        });
    }

    private void deleteUserDetails(final String uid, final DeleteUserListener listener) {
        ref.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure("delete user has failed: " + task.getException().getMessage());
                }
                else {
                    listener.onSuccess();
                }
            }
        });
    }
}
