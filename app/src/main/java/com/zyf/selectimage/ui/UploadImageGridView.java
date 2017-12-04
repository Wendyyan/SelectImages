package com.zyf.selectimage.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zyf.selectimage.R;
import com.zyf.selectimage.adapter.AddImageListAdapter;
import com.zyf.selectimage.model.ImageModel;

import java.util.ArrayList;

/**
 * Created by zyf on 2017/12/4.
 */

public class UploadImageGridView extends NoScrollGridView {

    public Context context;
    private UploadImageListener mUploadImageListener;
    AddImageListAdapter addImageListAdapter;

    public UploadImageGridView(Context context) {
        super(context);
    }

    public UploadImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UploadImageGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUploadImageListener(UploadImageListener uploadImageListener) {
        mUploadImageListener = uploadImageListener;
    }

    public void setAddImageListAdapter(AddImageListAdapter adapter) {
        addImageListAdapter = adapter;
        this.setAdapter(adapter);
        addImageListAdapter.addImagesListener = new AddImageListAdapter.AddImagesListener() {
            @Override
            public void addImage() {
                showBottomDialog();
            }

            @Override
            public void deleteImage(String uriPath) {
                if (mUploadImageListener != null) {
                    mUploadImageListener.deleteImage(uriPath);
                }
            }

            @Override
            public void previewImage(Context context, int position) {
                ArrayList<String> imageList = new ArrayList<>();
                for (ImageModel addProductImageListModel : addImageListAdapter.getData())
                    imageList.add(addProductImageListModel.imgUrl.replace(".tnl", ".jpg"));
                if (mUploadImageListener != null)
                    mUploadImageListener.previewImage(imageList, position);
            }
        };
    }

    /**
     * 弹出选择框
     */
    private void showBottomDialog() {
        BottomChooseDialog mCustomDialog = new BottomChooseDialog(this.getContext(),
                R.style.alert_dialog, R.layout.dialog_bottom_choose,
                this.getContext().getString(R.string.info_choose_from_camera),
                this.getContext().getString(R.string.info_choose_from_album),
                new BottomChooseDialog.BottomDialogListener() {
                    @Override
                    public void onChoose1(View v) {
                        if (mUploadImageListener != null)
                            mUploadImageListener.selectCamera();
                    }

                    @Override
                    public void onChoose2(View v) {
                        if (mUploadImageListener != null)
                            mUploadImageListener.selectAlbum();
                    }
                }, true);

        mCustomDialog.show();
    }

    public interface UploadImageListener{

        void selectCamera();

        void selectAlbum();

        void deleteImage(String uriPath);

        /**
         * @param imageList
         * @param selectPosition
         */
        void previewImage(ArrayList<String> imageList, int selectPosition);
    }
}
