package com.zyf.selectimage.model;

/**
 * Created by zyf on 2017/12/4.
 */

public class PhotoWallModel {

    public String path;
    public boolean isCheck;

    public String getPath() {
        return path;
    }

    public PhotoWallModel(String path, boolean isCheck) {
        this.path = path;
        this.isCheck = isCheck;
    }


}
