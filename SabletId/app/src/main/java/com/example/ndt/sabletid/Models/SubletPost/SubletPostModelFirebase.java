package com.example.ndt.sabletid.Models.SubletPost;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class SubletPostModelFirebase {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("subletPosts");

    private ValueEventListener getAllPostsEventListener;
    private ValueEventListener getPostByIdEventListener;
    private ValueEventListener getPostsByUserIdEventListener;

    public interface AddPostListener {
        void onSuccess(SubletPost post);

        void onFailure(String errorMessage);
    }

    public interface DeletePostListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface UpdateSubletPostListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public interface GetAllSubletPostsListener {
        void onSuccess(List<SubletPost> subletPosts);
    }

    public interface GetSubletPostByIdListener {
        void onSuccess(SubletPost post);
    }

    public interface GetSubletPostsByUserIdListener {
        void onSuccess(List<SubletPost> subletPosts);
    }

    public void addSubletPost(final SubletPost subletPost, final AddPostListener listener) {
        DatabaseReference pushedPostRef = ref.push();
        final String postId = pushedPostRef.getKey();
        pushedPostRef.setValue(subletPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure(task.getException().getMessage());
                }
                else {
                    subletPost.setId(postId);
                    listener.onSuccess(subletPost);
                }
            }
        });
    }

    public void deleteSubletPost(final SubletPost subletPost, final DeletePostListener listener) {
        ref.child(subletPost.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure(task.getException().getMessage());
                }
                else {
                    listener.onSuccess();
                }
            }
        });
    }

    public void updateUser(final SubletPost subletPost, final UpdateSubletPostListener listener) {
        ref.child(subletPost.getId()).setValue(subletPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    listener.onFailure(task.getException().getMessage());
                }
                else {
                    listener.onSuccess();
                }
            }
        });
    }

    public void getAllSubletPosts(final GetAllSubletPostsListener listener) {
        getAllPostsEventListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SubletPost> posts = new LinkedList<>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    SubletPost currentSubletPost = stSnapshot.getValue(SubletPost.class);
                    posts.add(currentSubletPost);
                }

                listener.onSuccess(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancelGetAllSubletPosts() {
        if (getAllPostsEventListener != null) {
            ref.removeEventListener(getAllPostsEventListener);
        }
    }

    public void getSubletPostsByUserId(final String userId, final GetSubletPostsByUserIdListener listener) {
        getPostsByUserIdEventListener = ref.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SubletPost> posts = new LinkedList<>();
                for (DataSnapshot stSnapshot : dataSnapshot.getChildren()) {
                    SubletPost currentSubletPost = stSnapshot.getValue(SubletPost.class);
                    posts.add(currentSubletPost);
                }

                listener.onSuccess(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cancelGetSubletPostsByUserId() {
        if (getPostsByUserIdEventListener != null) {
            ref.removeEventListener(getPostsByUserIdEventListener);
        }
    }

    public void getPostById(final String postId, final GetSubletPostByIdListener listener) {
        getPostByIdEventListener = ref.child(postId).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SubletPost post = dataSnapshot.getValue(SubletPost.class);

                if (post != null) {
                    listener.onSuccess(post);
                } else {
                    listener.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancelGetPostById() {
        if (getPostByIdEventListener != null) {
            ref.removeEventListener(getPostByIdEventListener);
        }
    }
}
