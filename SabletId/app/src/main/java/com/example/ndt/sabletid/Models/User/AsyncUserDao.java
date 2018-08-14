package com.example.ndt.sabletid.Models.User;

import android.os.AsyncTask;

import com.example.ndt.sabletid.Models.AppDatabase;

public class AsyncUserDao {
    public interface AsyncUserDaoListener<T> {
        void onComplete(T data);
    }

    static public void GetUserById(final String userId, final AsyncUserDaoListener<User> listener) {
        class UserAsyncTask extends AsyncTask<String, String, User> {
            @Override
            protected User doInBackground(String... strings) {
                User user = new User();
                if (AppDatabase.getInstance().userDao() != null) {
                   User userToCheck = AppDatabase.getInstance().userDao().getUserById(userId);

                    if (userToCheck != null) {
                        user = userToCheck;
                    }
                }

                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                listener.onComplete(user);
            }
        }

        UserAsyncTask task = new UserAsyncTask();
        task.execute();
    }

    static public void insert(final User user, final AsyncUserDaoListener<Boolean> listener) {
        class UserAsyncTask extends AsyncTask<User, String, Boolean> {
            @Override
            protected Boolean doInBackground(User... users) {
                if (users != null && users[0] != null) {
                    AppDatabase.getInstance().userDao().insertAll(users);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        UserAsyncTask task = new UserAsyncTask();
        task.execute(user);
    }

    static public void delete(final User user, final AsyncUserDaoListener<Boolean> listener) {
        class UserAsyncTask extends AsyncTask<User, String, Boolean> {

            @Override
            protected Boolean doInBackground(User... users) {
                if (users != null && users[0] != null) {
                    AppDatabase.getInstance().userDao().delete(users[0]);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        UserAsyncTask task = new UserAsyncTask();
        task.execute(user);
    }
}
