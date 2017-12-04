package com.zyf.selectimage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyf on 2017/12/1.
 */

public class ImageModel implements Parcelable {

    public String imgUrl;

    public ImageModel(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    protected ImageModel(Parcel in) {
        imgUrl = in.readString();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
    }
}
