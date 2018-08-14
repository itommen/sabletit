package com.example.ndt.sabletid.Models.User;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class UserModel {
    public static UserModel instance = new UserModel();

    private UserModelFirebase userModelFirebase;
    private FirebaseUser connectedFirebaseUser;
    ConnectedUser connectedUser;

    private UserModel() {
        userModelFirebase = new UserModelFirebase();
    }

    private class ConnectedUser extends MutableLiveData<User> {
        public ConnectedUser() {
            super();
            setValue(null);
        }

        @Override
        protected void onActive() {
            super.onActive();

            connectedFirebaseUser = userModelFirebase.getCurrentUser();

            if (connectedFirebaseUser == null) {
                setValue(null);
            }
            else {
                final String id = connectedFirebaseUser.getUid();
                AsyncUserDao.GetUserById(id, new AsyncUserDao.AsyncUserDaoListener<User>() {
                    @Override
                    public void onComplete(User data) {
                        if (data != null) {
                            setValue(data);
                        }

                        userModelFirebase.getUserById(id, new UserModelFirebase.GetUserByIdListener() {
                            @Override
                            public void onSuccess(User user) {
                                setValue(user);

                                AsyncUserDao.insert(user, new AsyncUserDao.AsyncUserDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean data) {
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
    }

    public LiveData<User> getConnectedUser() {
        if (this.connectedUser == null) {
            this.connectedUser = new ConnectedUser();
        }

        return this.connectedUser;
    }

    public interface LoginListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void login(final String email, final String password, final LoginListener listener) {
        userModelFirebase.loginWithEmailAndPassword(email, password, new UserModelFirebase.LoginListener() {
            @Override
            public void onSuccess() {
                connectedUser = new ConnectedUser();
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

    public interface DeleteUserListener {
        void OnSuccess();

        void OnFailure(String errorMessage);
    }

    public void deleteUser(final FirebaseUser firebaseUser, final User user, final DeleteUserListener listener) {
        userModelFirebase.deleteUser(firebaseUser, new UserModelFirebase.DeleteUserListener(){
            @Override
            public void onSuccess() {
                AsyncUserDao.delete(user, new AsyncUserDao.AsyncUserDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.OnSuccess();
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
