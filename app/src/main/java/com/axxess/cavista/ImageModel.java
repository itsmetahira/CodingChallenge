package com.axxess.cavista;

import java.util.ArrayList;

public class ImageModel {
    public static final String IS_ALBUM_KEY = "is_album";
    public static final String IMAGES_KEY = "images";
    public static final String IMAGE_ID_KEY = "id";
    public static final String IMAGE_URL_KEY = "link";
    public static final String IMAGE_TITLE_KEY = "title";

    private String mImageId;
    private String mImageUrl;
    private String mImageTitle;



    private ArrayList<String> mComments;

    public String getImageId() {
        return mImageId;
    }

    public void setImageId(String mImageId) {
        this.mImageId = mImageId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public ArrayList<String> getComments() {
        return mComments;
    }

    public void setComments(ArrayList<String> mComments) {
        this.mComments = mComments;
    }

    /**
     * Add a new comment to the comment list
     * @param comment
     */
    public void addComment(String comment) {
        if(mComments == null) {
            mComments = new ArrayList<String>();
        }
        this.mComments.add(comment);
    }

    public String getImageTitle() {
        return mImageTitle;
    }

    public void setImageTitle(String mImageTitle) {
        this.mImageTitle = mImageTitle;
    }
}
