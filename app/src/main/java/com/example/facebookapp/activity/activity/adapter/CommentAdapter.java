package com.example.facebookapp.activity.activity.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookapp.R;

import com.example.facebookapp.activity.activity.fragment.bottomsheets.SubCommentBottomSheet;
import com.example.facebookapp.activity.activity.model.CommentModel;
import com.example.facebookapp.activity.activity.model.PostModel;
import com.example.facebookapp.activity.activity.util.AgoDateParse;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<CommentModel.Result> results;
    PostModel postModel;

    public CommentAdapter(Context context, List<CommentModel.Result> results, PostModel postModel) {
        this.context = context;
        this.results = results;
        this.postModel = postModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CommentModel.Result result = results.get(position);
        holder.commentPerson.setText(result.getComment().getName());
        holder.commentBody.setText(result.getComment().getComment());

        if (!result.getComment().getProfileUrl().equals("")) {
            Picasso.get().load(result.getComment().getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.commentProfile, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(result.getComment().getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.commentProfile);
                }

            });
        }

        try {
            holder.commentDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(result.getComment().getCommentDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(result.getComment().getHasSubComment().equals("1")){
            holder.subCommentSection.setVisibility(View.VISIBLE);
            int commentTotal = Integer.parseInt(result.getSubComments().getTotal());
            if(commentTotal==1){
                holder.moreComments.setVisibility(View.GONE);
            }else{
                holder.moreComments.setVisibility(View.VISIBLE);
                commentTotal--;
                holder.moreComments.setText("View "+commentTotal+" more comments");
            }

            holder.subCommentBody.setText(result.getSubComments().getLastComment().get(0).getComment());
            holder.subCommentPerson.setText(result.getSubComments().getLastComment().get(0).getName());

            if (!result.getSubComments().getLastComment().get(0).getProfileUrl().equals("")) {
                Picasso.get().load(result.getSubComments().getLastComment().get(0).getProfileUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_default_user).into(holder.subCommentProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(result.getSubComments().getLastComment().get(0).getProfileUrl()).placeholder(R.drawable.img_default_user).into(holder.subCommentProfile);
                    }

                });
            }

            try {
                holder.subCommentDate.setText(AgoDateParse.getTimeAgo(AgoDateParse.getTimeInMillsecond(result.getSubComments().getLastComment().get(0).getCommentDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else{
            holder.subCommentSection.setVisibility(View.GONE);
        }
        holder.replyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new SubCommentBottomSheet();
                Bundle args = new Bundle();
                args.putParcelable("postModel", Parcels.wrap(postModel));
                args.putParcelable("commentModel", Parcels.wrap(results.get(position).getComment()));
                args.putBoolean("openkeyBoard",true);
                bottomSheetDialogFragment.setArguments(args);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "commentFragment");
            }
        });

        holder.moreComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new SubCommentBottomSheet();
                Bundle args = new Bundle();
                args.putParcelable("postModel", Parcels.wrap(postModel));
                args.putParcelable("commentModel", Parcels.wrap(results.get(position).getComment()));
                args.putBoolean("openkeyBoard",false);
                bottomSheetDialogFragment.setArguments(args);
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                bottomSheetDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "commentFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_profile)
        ImageView commentProfile;
        @BindView(R.id.comment_person)
        TextView commentPerson;
        @BindView(R.id.option_id)
        ImageView optionId;
        @BindView(R.id.comment_body)
        TextView commentBody;
        @BindView(R.id.comment_date)
        TextView commentDate;
        @BindView(R.id.reply_txt)
        TextView replyTxt;
        @BindView(R.id.more_comments)
        TextView moreComments;
        @BindView(R.id.sub_comment_profile)
        ImageView subCommentProfile;
        @BindView(R.id.sub_comment_person)
        TextView subCommentPerson;
        @BindView(R.id.sub_comment_body)
        TextView subCommentBody;
        @BindView(R.id.sub_comment_date)
        TextView subCommentDate;
        @BindView(R.id.sub_comment_section)
        LinearLayout subCommentSection;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
