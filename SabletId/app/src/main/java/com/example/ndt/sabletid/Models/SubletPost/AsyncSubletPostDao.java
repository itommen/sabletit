package com.example.ndt.sabletid.Models.SubletPost;

import android.os.AsyncTask;

import com.example.ndt.sabletid.Models.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class AsyncSubletPostDao {
    public interface AsyncSubletPostDaoListener<T> {
        void onComplete(T data);
    }

    public static void getAll(final AsyncSubletPostDaoListener<List<SubletPost>> listener) {
        class SubletPostAsyncTask extends AsyncTask<String, String, List<SubletPost>> {
            @Override
            protected List<SubletPost> doInBackground(String... strings) {
                List<SubletPost> subletPostList = new ArrayList<SubletPost>();

                SubletPostDao dao = AppDatabase.getInstance().subletPostDao();

                if (dao != null) {
                    List<SubletPost> postList = AppDatabase.getInstance().subletPostDao().getAll();

                    if (postList != null) {
                        subletPostList = postList;
                    }
                }

                return subletPostList;
            }

            @Override
            protected void onPostExecute(List<SubletPost> subletPosts) {
                super.onPostExecute(subletPosts);
                listener.onComplete(subletPosts);
            }
        }
        SubletPostAsyncTask task = new SubletPostAsyncTask();
        task.execute();
    }

    public static void getSubletPostsByUserId(final String userId, final AsyncSubletPostDaoListener<List<SubletPost>> listener) {
        class SubletPostAsyncTask extends AsyncTask<String, String, List<SubletPost>> {
            @Override
            protected List<SubletPost> doInBackground(String... strings) {
                List<SubletPost> subletPostList = new ArrayList<SubletPost>();
                SubletPostDao dao = AppDatabase.getInstance().subletPostDao();

                if (dao != null) {
                    List<SubletPost> postList = AppDatabase.getInstance().subletPostDao().getSubletPostsByUserId(userId);

                    if (postList != null) {
                        subletPostList = postList;
                    }
                }

                return subletPostList;
            }

            @Override
            protected void onPostExecute(List<SubletPost> subletPosts) {
                super.onPostExecute(subletPosts);
                listener.onComplete(subletPosts);
            }
        }

        SubletPostAsyncTask task = new SubletPostAsyncTask();
        task.execute();
    }

    public static void insert(final List<SubletPost> posts, final AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean> listener) {
        class SubletPostAsyncTask extends AsyncTask<List<SubletPost>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<SubletPost>... subletPosts) {
                if (subletPosts != null) {
                    for (SubletPost sp:subletPosts[0]) {
                        AppDatabase.getInstance().subletPostDao().insertAll(sp);
                    }
                }

                return true;
            }


            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        SubletPostAsyncTask task = new SubletPostAsyncTask();
        task.execute(posts);
    }

    public static void update(final SubletPost subletPost, final AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean> listener) {
        class SubletPostAsyncTask extends AsyncTask<SubletPost, String, Boolean> {
            @Override
            protected Boolean doInBackground(SubletPost... subletPosts) {
                if (subletPosts != null && subletPosts[0] != null) {
                    AppDatabase.getInstance().subletPostDao().update(subletPosts);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        SubletPostAsyncTask task = new SubletPostAsyncTask();
        task.execute(subletPost);
    }

    public static void delete(final SubletPost subletPost, final AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean> listener) {
        class SubletPostAsyncTask extends AsyncTask<SubletPost, String, Boolean> {

            @Override
            protected Boolean doInBackground(SubletPost... subletPosts) {
                if (subletPosts != null && subletPosts[0] != null) {
                    AppDatabase.getInstance().subletPostDao().delete(subletPosts[0]);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }

        SubletPostAsyncTask task = new SubletPostAsyncTask();
        task.execute(subletPost);
    }
}
