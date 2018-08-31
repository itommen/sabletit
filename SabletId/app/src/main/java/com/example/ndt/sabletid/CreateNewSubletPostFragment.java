package com.example.ndt.sabletid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;
import com.example.ndt.sabletid.ViewModels.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

public class CreateNewSubletPostFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseUser connectedUser;
    private SubletPostViewModel subletPostViewModel;
    private UserViewModel userViewModel;
    private Bitmap imageBitmap;

    ImageView ivImage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_new_sublet_post, container, false);

        view.findViewById(R.id.btnCreateSubletPostEditImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ivImage.setImageBitmap(imageBitmap);
        }
    }
}
