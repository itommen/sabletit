package com.example.ndt.sabletid.Models.SubletPost;

import android.arch.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SubletPostModel {
    private SubletPostModelFirebase subletPostModelFirebase;

    public static SubletPostModel instance = new SubletPostModel();
    public SubletPostList subletPostList = new SubletPostList();

    private SubletPostModel() {
        subletPostModelFirebase = new SubletPostModelFirebase();
    }

    public interface DeleteSubletPostListener {
        void OnSuccess();

        void OnFailure(String errorMessage);
    }

    public interface UpdateSubletPostListener {
        void OnSuccess();

        void OnFailure(String errorMessage);
    }

    public interface AddSubletPostListener {
        void OnSuccess(SubletPost subletPost);

        void OnFailure(String errorMessage);
    }

    public void deleteSubletPost(final SubletPost subletPost, final DeleteSubletPostListener listener) {
        subletPostModelFirebase.deleteSubletPost(subletPost, new SubletPostModelFirebase.DeletePostListener() {
            @Override
            public void onSuccess() {
                AsyncSubletPostDao.delete(subletPost, new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
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

    public void updateSubletPost(final SubletPost subletPost, final UpdateSubletPostListener listener) {
        subletPostModelFirebase.updateUser(subletPost, new SubletPostModelFirebase.UpdateSubletPostListener() {
            @Override
            public void onSuccess() {
                AsyncSubletPostDao.update(subletPost, new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
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


    public void addSubletPost(final SubletPost subletPost, final AddSubletPostListener listener) {
        subletPostModelFirebase.addSubletPost(subletPost, new SubletPostModelFirebase.AddPostListener() {

            @Override
            public void onSuccess() {
                AsyncSubletPostDao.insert(Arrays.asList(subletPost), new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.OnSuccess(subletPost);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                listener.OnFailure(errorMessage);
            }
        });
    }

    public class SubletPostList extends MutableLiveData<List<SubletPost>> {
        @Override
        protected void onActive() {
            super.onActive();

            AsyncSubletPostDao.getAll(new AsyncSubletPostDao.AsyncSubletPostDaoListener<List<SubletPost>>() {
                @Override
                public void onComplete(List<SubletPost> data) {
                    setValue(data);

                    subletPostModelFirebase.getAllSubletPosts(new SubletPostModelFirebase.GetAllSubletPostsListener() {
                        @Override
                        public void onSuccess(List<SubletPost> subletPosts) {
                            setValue(subletPosts);

                            AsyncSubletPostDao.insert(subletPosts, new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {

                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }

        public SubletPostList() {
            super();

            setValue(new LinkedList<SubletPost>());
        }
    }



}
