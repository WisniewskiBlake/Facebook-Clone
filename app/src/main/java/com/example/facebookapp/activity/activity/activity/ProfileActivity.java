package com.example.facebookapp.activity.activity.activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.facebookapp.R;
import com.example.facebookapp.activity.activity.adapter.ProfileViewPagerAdapter;
import com.example.facebookapp.activity.activity.model.User;
import com.example.facebookapp.activity.activity.rest.ApiClient;
import com.example.facebookapp.activity.activity.rest.services.UserInterface;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.profile_cover)
    ImageView profileCover;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_option_btn)
    Button profileOptionBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ViewPager_profile)
    ViewPager ViewPagerProfile;

    ProfileViewPagerAdapter profileViewPagerAdapter;

      /*

    0 = profile is still loading
    1=  two people are friends ( unfriend )
    2 = this person has sent friend request to another friend ( cancel sent requeset )
    3 = this person has received friend request from another friend  (  reject or accept request )
    4 = people are unkown ( you can send requeset )
    5 = own profile
     */

    int current_state = 0;
    String profileUrl = "";
    String coverUrl = "";

    ProgressDialog progressDialog;
    int imageUploadType = 0;

    String uid = "0";
    File compressedImageFile;

    String cover = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For hiding status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        uid = getIntent().getStringExtra("uid");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(uid)) {
            // UID is matched , we are going to load our own profile
            current_state = 5;
            profileOptionBtn.setText("Edit Profile");
            loadProfile();
        } else {

            otherOthersProfile();
            // load others profile here
        }
        profileOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileOptionBtn.setEnabled(false);
                if (current_state == 5) {
                    CharSequence options[] = new CharSequence[]{"Change Cover Picture", "Change Profile Picture", "View Cover Picture", "View Profile Picture", "Sign Out"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setOnDismissListener(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                imageUploadType = 1;
                                ImagePicker.create(ProfileActivity.this)
                                        .folderMode(true)
                                        .single()
                                        .toolbarFolderTitle("Choose a folder")
                                        .toolbarImageTitle("Select a Image")
                                        .start();
                                //Change cover part
                            } else if (position == 1) {
                                imageUploadType = 0;
                                ImagePicker.create(ProfileActivity.this)
                                        .folderMode(true)
                                        .single().toolbarFolderTitle("Choose a folder").toolbarImageTitle("Select a Image")
                                        .start();
                                //Change  profile part
                            } else if (position == 2) {
                                viewFullImage(profileCover, coverUrl);
                                //view cover proifle
                            } else if (position == 3) {
                                viewFullImage(profileImage, profileUrl);
                                //view profile picture
                            } else {
                                signOut();
                            }
                        }
                    });
                    builder.show();
                } else if (current_state == 4) {
                    profileOptionBtn.setText("Processing...");
                    CharSequence options[] = new CharSequence[]{"Send Friend Request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setOnDismissListener(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                performAction(current_state);
                            }
                        }
                    });
                    builder.show();
                }else if(current_state==2){
                    profileOptionBtn.setText("Processing...");
                    CharSequence options[] = new CharSequence[]{"Cancel Friend Request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setOnDismissListener(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                performAction(current_state);
                            }
                        }
                    });
                    builder.show();
                }else if(current_state==3){
                    profileOptionBtn.setText("Processing...");
                    CharSequence options[] = new CharSequence[]{"Accepet Friend Request"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setOnDismissListener(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                performAction(current_state);
                            }
                        }
                    });
                    builder.show();
                }else if(current_state ==1){
                    profileOptionBtn.setText("Processing...");
                    CharSequence options[] = new CharSequence[]{"Unfriend this User"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setOnDismissListener(ProfileActivity.this);
                    builder.setTitle("Choose Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                performAction(current_state);
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }

    private void performAction(final int i) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<Integer> call = userInterface.performAction(new PerformAction(i + "", FirebaseAuth.getInstance().getCurrentUser().getUid(), uid));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                profileOptionBtn.setEnabled(true);
                if (response.body() == 1) {
                    if (i == 4) {
                        current_state = 2;
                        profileOptionBtn.setText("Request Sent");
                        Toast.makeText(ProfileActivity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                    }else if(i==2){
                        current_state = 4;
                        profileOptionBtn.setText("Send Request");
                        Toast.makeText(ProfileActivity.this, "Request Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    }else if(i==3){
                        current_state = 1;
                        profileOptionBtn.setText("Friends");
                        Toast.makeText(ProfileActivity.this, "You are now friends on Blakebook!", Toast.LENGTH_SHORT).show();
                    }else if(i==1){
                        current_state =4;
                        profileOptionBtn.setText("Send Request");
                        Toast.makeText(ProfileActivity.this, "You are no longer friends", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    profileOptionBtn.setEnabled(false);
                    profileOptionBtn.setText("Error...");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    private void otherOthersProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("profileId", uid);

        Call<User> call = userInterface.loadOtherProfile(params);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull final Response<User> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    coverUrl = response.body().getCoverUrl();
                    showUserData(response.body());

                    if (response.body().getState().equalsIgnoreCase("1")) {
                        profileOptionBtn.setText("Friends");
                        current_state = 1;
                    } else if (response.body().getState().equalsIgnoreCase("2")) {
                        profileOptionBtn.setText("Cancel Request");
                        current_state = 2;
                    } else if (response.body().getState().equalsIgnoreCase("3")) {
                        current_state = 3;
                        profileOptionBtn.setText("Accept Request");
                    } else if (response.body().getState().equalsIgnoreCase("4")) {
                        current_state = 4;
                        profileOptionBtn.setText("Send Request");
                    } else {
                        current_state = 0;
                        profileOptionBtn.setText("Error");
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();
            }
        });
        // Toast.makeText(Pro
    }

    private void showUserData(User user) {
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(), 1,user.getUid(),user.getState());
        ViewPagerProfile.setAdapter(profileViewPagerAdapter);

        profileUrl = user.getProfileUrl();

        collapsingToolbar.setTitle(user.getName());
        if (!profileUrl.isEmpty()) {
            Picasso.get().load(profileUrl).into(profileImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    System.out.println("Success");
                }
                @Override
                public void onError(Exception e) {
                    System.out.println("Error");
                    Picasso.get().load(profileUrl).into(profileImage);
                }
            });

            if (!coverUrl.isEmpty()) {
                Picasso.get().load(coverUrl).into(profileCover, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Success");
                    }
                    @Override
                    public void onError(Exception e) {
                        System.out.println("Error");
                        Picasso.get().load(coverUrl).into(profileCover);
                    }
                });
            }

            addImageCoverClick();
        }
    }

    private void viewFullImage(View view, String link) {
        Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
        intent.putExtra("imageUrl", link);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(view, "shared");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }


    private void loadProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<User> call = userInterface.loadownProfile(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    coverUrl = response.body().getCoverUrl();
                    showUserData(response.body());

                } else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Something went wrong ... Please try later", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addImageCoverClick() {
        profileCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFullImage(profileCover, coverUrl);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFullImage(profileImage, profileUrl);
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        profileOptionBtn.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);

            try {
                compressedImageFile = new Compressor(this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));


                uploadFile(compressedImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(final File compressedImageFile) {

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("postUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        builder.addFormDataPart("imageUploadType", imageUploadType + "");
        builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile));

        String pUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String iUpload = imageUploadType + "";
        String filee = compressedImageFile.getName();

        MultipartBody multipartBody = builder.build();

        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<Integer> call = userInterface.uploadImage(multipartBody);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body() == 1) {
                    if (imageUploadType == 0) {
                        Picasso.get().load(compressedImageFile).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(profileImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(compressedImageFile).placeholder(R.drawable.default_image_placeholder).into(profileImage);
                            }
                        });
                        Toast.makeText(ProfileActivity.this, "Profile Picture Changed Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Picasso.get().load(compressedImageFile).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(profileCover, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(compressedImageFile).placeholder(R.drawable.default_image_placeholder).into(profileCover);
                            }
                        });
                        Toast.makeText(ProfileActivity.this, "Cover Picture Changed Successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public static class   PerformAction {
        String operationType, userId, profileid;

        public PerformAction(String operationType, String userId, String profileid) {
            this.operationType = operationType;
            this.userId = userId;
            this.profileid = profileid;
        }
    }
}


