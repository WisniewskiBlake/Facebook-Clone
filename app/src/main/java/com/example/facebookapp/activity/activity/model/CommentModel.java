package com.example.facebookapp.activity.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

public class CommentModel {
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("comment")
        @Expose
        private Comment comment;
        @SerializedName("subComments")
        @Expose
        private SubComments subComments;

        public Comment getComment() {
            return comment;
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }

        public SubComments getSubComments() {
            return subComments;
        }

        public void setSubComments(SubComments subComments) {
            this.subComments = subComments;
        }

    }


    public class SubComments {

        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("lastComment")
        @Expose
        private List<LastComment> lastComment = null;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<LastComment> getLastComment() {
            return lastComment;
        }

        public void setLastComment(List<LastComment> lastComment) {
            this.lastComment = lastComment;
        }

    }


    @Parcel
    public static   class Comment {

        @SerializedName("cid")
        @Expose
        private String cid;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("commentBy")
        @Expose
        private String commentBy;
        @SerializedName("commentDate")
        @Expose
        private String commentDate;
        @SerializedName("superParentId")
        @Expose
        private String superParentId;
        @SerializedName("parentId")
        @Expose
        private String parentId;
        @SerializedName("hasSubComment")
        @Expose
        private String hasSubComment;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profileUrl")
        @Expose
        private String profileUrl;
        @SerializedName("userToken")
        @Expose
        private String userToken;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommentBy() {
            return commentBy;
        }

        public void setCommentBy(String commentBy) {
            this.commentBy = commentBy;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public String getSuperParentId() {
            return superParentId;
        }

        public void setSuperParentId(String superParentId) {
            this.superParentId = superParentId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getHasSubComment() {
            return hasSubComment;
        }

        public void setHasSubComment(String hasSubComment) {
            this.hasSubComment = hasSubComment;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

    }

    public class LastComment {

        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("commentBy")
        @Expose
        private String commentBy;
        @SerializedName("commentDate")
        @Expose
        private String commentDate;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profileUrl")
        @Expose
        private String profileUrl;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommentBy() {
            return commentBy;
        }

        public void setCommentBy(String commentBy) {
            this.commentBy = commentBy;
        }

        public String getCommentDate() {
            return commentDate;
        }

        public void setCommentDate(String commentDate) {
            this.commentDate = commentDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

    }
}
