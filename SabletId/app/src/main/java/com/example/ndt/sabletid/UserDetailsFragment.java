package com.example.ndt.sabletid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.Image.ImageModel;
import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.ViewModels.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

public class UserDetailsFragment extends Fragment {
    private static final String ARG_NAME = "ARG_NAME";
    private static final String ARG_EMAIL = "ARG_EMAIL";
    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_GENDER = "ARG_GENDER";
    private static final String ARG_URL = "InstanceImage.png";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private UserViewModel userViewModel;
    private FirebaseUser firebaseUser;
    private User connectedUser;
    private Bitmap imageBitmap;

    ImageView ivImage;
    EditText etUserName;
    EditText etEmail;
    EditText etPhone;
    AppCompatRadioButton rbMale;
    AppCompatRadioButton rbFemale;

    Boolean isImageChanged = false;
    Boolean isInstanceState = false;
    String avatarUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        etUserName = view.findViewById(R.id.etUserNameUserDetails);
        etEmail = view.findViewById(R.id.etEmailUserDetails);
        etPhone = view.findViewById(R.id.etPhoneUserDetails);
        rbMale = view.findViewById(R.id.radioRegisterMale);
        rbFemale = view.findViewById(R.id.radioRegisterFemale);
        ivImage = view.findViewById(R.id.ivImage);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        userViewModel.getConnectedUser().observe(UserDetailsFragment.this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                firebaseUser = userViewModel.getFirebaseUser();
                connectedUser = user;

                if (user != null) {
                    etUserName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());

                    if (user.getGender()) {
                        rbFemale.setChecked(true);
                    }
                    else {
                        rbMale.setChecked(true);
                    }

                    avatarUrl = user.getAvatar();
                    if (!isImageChanged && !isInstanceState && avatarUrl != null) {
                        ImageModel.instance.getImage(avatarUrl, new ImageModel.GetImageListener() {
                            @Override
                            public void onDone(Bitmap bitmap) {
                                if (bitmap != null) {
                                    ivImage.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                }
            }
        });

        view.findViewById(R.id.btnDeleteUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userViewModel.deleteUser(firebaseUser, connectedUser, new UserViewModel.DeleteUserListener() {
                   @Override
                   public void onSuccess() {
                       Toast.makeText(view.getContext(), "Your account has been deleted successfully",
                               Toast.LENGTH_LONG).show();

                       Fragment newFragment = new LoginFragment();
                       FragmentTransaction transaction = getFragmentManager().beginTransaction();

                       transaction.replace(R.id.content_frame, newFragment);
                       transaction.addToBackStack(null);

                       transaction.commit();
                   }

                   @Override
                   public void onFailure(String errorMessage) {
                       Toast.makeText(view.getContext(), "Failed to delete user, please try again",
                               Toast.LENGTH_LONG).show();
                   }
               });
            }
        });

        view.findViewById(R.id.btnUpdateUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User();
                user.setId(connectedUser.getId());
                user.setName(etUserName.getText().toString().trim());
                user.setEmail(etEmail.getText().toString().trim());
                user.setPhone(etPhone.getText().toString().trim());
                user.setGender(rbFemale.isChecked());
                user.setAvatar(avatarUrl);

                if (imageBitmap != null) {
                    ImageModel.instance.saveImage(imageBitmap, new ImageModel.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            user.setAvatar(url);

                            updateUser(firebaseUser, user);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(view.getContext(), "Failed to update user, please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    updateUser(firebaseUser, user);
                }

                if (savedInstanceState != null) {
                    savedInstanceState.putString(ARG_NAME, etUserName.getText().toString().trim());
                    savedInstanceState.putString(ARG_EMAIL, etEmail.getText().toString().trim());
                    savedInstanceState.putString(ARG_PHONE, etPhone.getText().toString().trim());
                    savedInstanceState.putBoolean(ARG_GENDER, rbFemale.isChecked());

//                    ImageModel.instance.saveImageToFile(imageBitmap, ARG_URL);
                }
            }
        });

        view.findViewById(R.id.btnEditImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_IMAGE_CAPTURE);
                }
                else {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        return view;
    }

    public void updateUser(FirebaseUser firebaseUser, User user) {
        userViewModel.updateUser(firebaseUser, user, new UserViewModel.UpdateUserListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getView().getContext(), "Your account has been updated successfully",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getView().getContext(), "Failed to update user, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivImage.setImageBitmap(imageBitmap);

            isImageChanged = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Toast.makeText(getView().getContext(), "Can't open camera without permissions",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        etUserName.setText(savedInstanceState.getString(ARG_NAME));
        etEmail.setText(savedInstanceState.getString(ARG_EMAIL));
        etPhone.setText(savedInstanceState.getString(ARG_PHONE));
        rbFemale.setChecked(savedInstanceState.getBoolean(ARG_GENDER));
        rbMale.setChecked(!savedInstanceState.getBoolean(ARG_GENDER));

//        Bitmap bitMap = ImageModel.instance.loadImageFromFile(ARG_URL);
//        if (bitMap != null) {
//            ivImage.setImageBitmap(bitMap);
//            imageBitmap = bitMap;
//            ImageModel.instance.DeleteImage(ARG_URL);
//        }
//
//        isInstanceState = true;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putString(ARG_NAME, etUserName.getText().toString().trim());
        bundle.putString(ARG_EMAIL, etEmail.getText().toString().trim());
        bundle.putString(ARG_PHONE, etPhone.getText().toString().trim());
        bundle.putBoolean(ARG_GENDER, rbFemale.isChecked());

//        ImageModel.instance.saveImageToFile(imageBitmap, ARG_URL);
    }
}
