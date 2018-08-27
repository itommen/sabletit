package com.example.ndt.sabletid.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.Models.SubletPost.SubletPostModel;

import java.util.List;

public class SubletPostViewModel extends ViewModel {
    private LiveData<List<SubletPost>> subletPostListLiveData;
    private LiveData<List<SubletPost>> subletPostListByUserIdLiveData;
    private LiveData<SubletPost> subletPostByIdLiveData;

    public LiveData<List<SubletPost>> getAllSubletPosts() {
        subletPostListLiveData = SubletPostModel.instance.getAllSubletPosts();

        return subletPostListLiveData;
    }

    public LiveData<List<SubletPost>> getSubletPostsByUserId(String userId) {
        SubletPostModel.instance.InitUserId(userId);
        subletPostListByUserIdLiveData = SubletPostModel.instance.getAllSubletPostsByUserId();

        return subletPostListByUserIdLiveData;
    }

    public LiveData<SubletPost> getSubletPostById(String id) {
        SubletPostModel.instance.InitSubletPostId(id);
        subletPostByIdLiveData = SubletPostModel.instance.getSubletPostById();

        return subletPostByIdLiveData;
    }

    public void deleteSubletPost(final SubletPost subletPost, final DeleteSubletPostListener listener) {
        SubletPostModel.instance.deleteSubletPost(subletPost,  new SubletPostModel.DeleteSubletPostListener() {
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

    public void updateSubletPost(final SubletPost subletPost, final UpdateSubletPostListener listener) {
        SubletPostModel.instance.updateSubletPost(subletPost, new SubletPostModel.UpdateSubletPostListener() {
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

    public void addNewSubletPost(final SubletPost subletPost, final AddNewSubletPostListener listener) {
        SubletPostModel.instance.addSubletPost(subletPost, new SubletPostModel.AddSubletPostListener() {
            @Override
            public void OnSuccess(SubletPost subletPost) {
                listener.onSuccess(subletPost);
            }

            @Override
            public void OnFailure(String errorMessage) {
                listener.onFailure(errorMessage);
            }
        });
    }

    public interface AddNewSubletPostListener {
        void onSuccess(SubletPost subletPost);

        void onFailure(String errorMessage);
    }

    public interface UpdateSubletPostListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface DeleteSubletPostListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}
