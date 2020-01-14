package com.example.facebookapp.activity.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.facebookapp.R;
import com.example.facebookapp.activity.activity.model.PostModel;
import com.example.facebookapp.activity.activity.rest.ApiClient;
import com.example.facebookapp.activity.activity.util.AgoDateParse;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullPostActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.post_user_image)
    ImageView postUserImage;
    @BindView(R.id.post_user_name)
    TextView postUserName;
    @BindView(R.id.privacy)
    ImageView privacy;
    @BindView(R.id.post_date)
    TextView postDate;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.post_image)
    ImageView postImage;
    @BindView(R.id.top_rel)
    RelativeLayout topRel;
    @BindView(R.id.like_img)
    ImageView likeImg;
    @BindView(R.id.like_txt)
    TextView likeTxt;
    @BindView(R.id.like_section)
    LinearLayout likeSection;
    @BindView(R.id.comment_txt)
    TextView commentTxt;
    @BindView(R.id.comment_section)
    LinearLayout commentSection;
    @BindView(R.id.reaction_card)
    CardView reactionCard;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.comment_send)
    ImageView commentSend;
    @BindView(R.id.comment_bottom_part)
    LinearLayout commentBottomPart;
    @BindView(R.id.top_hide_show)
    RelativeLayout topHideShow;

    PostModel postModel;

    boolean isFlagZero = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);
        ButterKnife.bind(this);
        postModel = Parcels.unwrap(getIntent().getBundleExtra("postBundle").getParcelable("postModel"));
        if(postModel==null){
            Toast.makeText(FullPostActivity.this,"Something went wrong !",Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
        }

        // Setting the tool with back button
        setSupportActionBar(toolbar);
        setTitle("");;
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setData(postModel);
    }


    private void setData(final PostModel postModel) {
        postUserName.setText(postModel.getName());
        status.setText(postModel.getPost());
        if (!postModel.getProfileUrl().isEmpty()) {
            Picasso.get().load(postModel.getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(postUserImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError(Exception e) {
                    Picasso.get().load(postModel.getProfileUrl()).placeholder(R.drawable.default_image_placeholder).error(R.drawable.default_image_placeholder).into(postUserImage);
                }
            });
        }
        if(!postModel.getStatusImage().isEmpty()){
            Picasso.get().load(ApiClient.BASE_URL_1+postModel.getStatusImage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(postImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError(Exception e) {
                    Picasso.get().load(ApiClient.BASE_URL_1+postModel.getStatusImage()).placeholder(R.drawable.default_image_placeholder).error(R.drawable.default_image_placeholder).into(postImage);
                }
            });
        }else{
            postImage.setVisibility(View.GONE);
        }

        try {
            postDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(postModel.getStatusTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(postModel.getPrivacy().equalsIgnoreCase("0")){
            privacy.setImageResource(R.drawable.icon_friends);
        }else if(postModel.getPrivacy().equalsIgnoreCase("1")){
            privacy.setImageResource(R.drawable.icon_onlyme);
        }else{
            privacy.setImageResource(R.drawable.icon_public);
        }

    }




}
