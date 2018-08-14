package com.example.ndt.sabletid.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.Models.User.UserModel;
import com.example.ndt.sabletid.Models.User.UserModelFirebase;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    LiveData<User> data;

    public interface LoginListener {
        void onSuccess();

        void onFailure(String exceptionMessage);
    }

    public void login(final String email, final String password, final LoginListener listener) {
        UserModel.instance.login(email, password, new UserModel.LoginListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailure(String exceptionMessage) {
                listener.onFailure(exceptionMessage);
            }
        });
    }

    public interface RegisterListener {
        void onSuccess(User user);

        void onFailure(String errorMessage);
    }

    public void registerUser(final User user, final String email, final String password, final RegisterListener listener) {
        UserModel.instance.register(user, email, password, new UserModel.RegisterListener() {
            @Override
            public void OnSuccess(User user) {
                listener.onSuccess(user);
            }

            @Override
            public void OnFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    public interface DeleteUserListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void deleteUser(final FirebaseUser firebaseUser, final User user, final DeleteUserListener listener) {
        UserModel.instance.deleteUser(firebaseUser, user, new UserModel.DeleteUserListener() {
            @Override
            public void OnSuccess() {
                listener.onSuccess();
            }

            @Override
            public void OnFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    public LiveData<User> getConnectedUser() {
        data = UserModel.instance.getConnectedUser();

        return data;
    }

    public FirebaseUser getFirebaseUser() {
        return UserModel.instance.connectedFirebaseUser;
    }
}
