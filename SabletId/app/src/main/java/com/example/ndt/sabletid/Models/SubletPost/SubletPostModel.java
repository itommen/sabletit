package com.example.ndt.sabletid.Models.SubletPost;

public class SubletPostModel {
    public static SubletPostModel instance = new SubletPostModel();

    private SubletPostModelFirebase subletPostModelFirebase;

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
                AsyncSubletPostDao.insert(subletPost, new AsyncSubletPostDao.AsyncSubletPostDaoListener<Boolean>() {
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
}
