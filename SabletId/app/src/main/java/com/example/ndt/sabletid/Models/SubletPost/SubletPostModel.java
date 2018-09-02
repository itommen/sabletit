package com.example.ndt.sabletid.Models.SubletPost;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SubletPostModel {
    private SubletPostModelFirebase subletPostModelFirebase;

    public static SubletPostModel instance = new SubletPostModel();

    public SubletPostList subletPostList = new SubletPostList();
    public SubletPostsByUserIdList subletPostsByUserIdList = new SubletPostsByUserIdList();
    public SubletPostById subletPostById = new SubletPostById();

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
            public void onSuccess(final SubletPost post) {
                AsyncSubletPostDao.insert(Arrays.asList(post), new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        listener.OnSuccess(post);
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
        public SubletPostList() {
            super();

            setValue(new LinkedList<SubletPost>());
        }

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
    }

    public LiveData<List<SubletPost>> getAllSubletPosts() {
        return subletPostList;
    }

    public class SubletPostsByUserIdList extends MutableLiveData<List<SubletPost>> {
        String userId = "";

        public SubletPostsByUserIdList() {
            super();

            setValue(new LinkedList<SubletPost>());
        }

        public void InitUserId(String userId) {
            this.userId = userId;
        }

        @Override
        protected void onActive() {
            AsyncSubletPostDao.getSubletPostsByUserId(userId, new AsyncSubletPostDao.AsyncSubletPostDaoListener<List<SubletPost>>() {
                @Override
                public void onComplete(List<SubletPost> data) {
                    setValue(data);
                    subletPostModelFirebase.getSubletPostsByUserId(userId, new SubletPostModelFirebase.GetSubletPostsByUserIdListener() {
                        @Override
                        public void onSuccess(List<SubletPost> subletPosts) {
                            setValue(subletPosts);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
    }

    public void InitUserId(String userId) {
        if (subletPostsByUserIdList == null) {
            subletPostsByUserIdList = new SubletPostsByUserIdList();
        }

        subletPostsByUserIdList.InitUserId(userId);
    }

    public LiveData<List<SubletPost>> getAllSubletPostsByUserId() {
        return subletPostsByUserIdList;
    }

    public class SubletPostById extends MutableLiveData<SubletPost> {
        String subletPostId = "";

        public SubletPostById() {
            super();
            setValue(new SubletPost());
        }

        public void InitSubletPostId(String subletPostId) {
            this.subletPostId = subletPostId;
        }

        @Override
        protected void onActive() {
            super.onActive();

            AsyncSubletPostDao.getSubletPostById(subletPostId, new AsyncSubletPostDao.AsyncSubletPostDaoListener<SubletPost>() {
                @Override
                public void onComplete(SubletPost data) {
                    setValue(data);

                    subletPostModelFirebase.getPostById(subletPostId, new SubletPostModelFirebase.GetSubletPostByIdListener() {
                        @Override
                        public void onSuccess(SubletPost post) {
                            setValue(post);
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
        }
    }

    public void InitSubletPostId(String subletPostId) {
        if (subletPostById == null) {
            subletPostById = new SubletPostById();
        }

        subletPostById.InitSubletPostId(subletPostId);
    }

    public LiveData<SubletPost> getSubletPostById() {
        return subletPostById;
    }
}
