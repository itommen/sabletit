package com.example.ndt.sabletid.Models.User;

import com.google.firebase.auth.FirebaseUser;

public class UserModel {
    public static UserModel instance = new UserModel();

    private UserModelFirebase userModelFirebase;
    private FirebaseUser currentFirebaseUser;

    private UserModel() {
        userModelFirebase = new UserModelFirebase();
    }

    public interface LoginListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void login(final String email, final String password, final LoginListener listener) {
        userModelFirebase.loginWithEmailAndPassword(email, password, new UserModelFirebase.LoginListener() {
            @Override
            public void onSuccess() {
                //userData = new UserData();
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public interface RegisterListener {
        void OnSuccess(User user);

        void OnFailure(String errorMessage);
    }

    public void register(User user, String email, String password, final RegisterListener listener) {
        userModelFirebase.createUserWithEmailAndPassword(user, email, password, new UserModelFirebase.RegisterListener(){

            @Override
            public void onSuccess(final User user) {
                AsyncUserDao.insert(user, new AsyncUserDao.AsyncUserDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.OnSuccess(user);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.OnFailure(errorMessage);
            }
        });
    }
}
