package com.example.ndt.sabletid.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.Models.User.UserModel;
import com.example.ndt.sabletid.Models.User.UserModelFirebase;

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
}
