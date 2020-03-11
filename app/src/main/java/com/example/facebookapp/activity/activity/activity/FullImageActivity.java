package com.example.facebookapp.activity.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.facebookapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {
    PhotoView mPhotoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        final String imageUrl = getIntent().getStringExtra("imageUrl");
        mPhotoView = findViewById(R.id.full_image_id);

        if(!imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(mPhotoView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(imageUrl).into(mPhotoView);
                }

            });
        }
    }
}
