package com.zyf.selectimage.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.os.Bundle;

import com.zyf.selectimage.R;
import com.zyf.selectimage.adapter.AddImageListAdapter;
import com.zyf.selectimage.model.ImageModel;
import com.zyf.selectimage.ui.UploadImageGridView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private UploadImageGridView uigUploadImage;

    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<ImageModel> mSelectedImgList = new ArrayList<>();
    private AddImageListAdapter addImageListAdapter;

    private String imagePath;
    private String mRandomPicName;
    private static final int IMAGE_STORE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uigUploadImage = (UploadImageGridView) findViewById(R.id.uig_upload_image);
        imagePath = getImageFolder();
        addImageListAdapter = new AddImageListAdapter(mSelectedImgList, this);
        uigUploadImage.setAddImageListAdapter(addImageListAdapter);
        uigUploadImage.setUploadImageListener(new UploadImageGridView.UploadImageListener() {
            @Override
            public void selectCamera() {
                //TODO
            }

            @Override
            public void selectAlbum() {
                Intent intent = new Intent(getApplicationContext(), PhotoAlbumActivity.class);
                intent.putExtra("selected_img_num", mSelectedImgList.size());
                startActivityForResult(intent, IMAGE_STORE);
            }

            @Override
            public void deleteImage(String uriPath) {
                if (mResults.size() > 0) {
                    for (int i = 0; i < mResults.size(); i++) {
                        if (mResults.get(i).equals(uriPath)) {
                            mResults.remove(i);
                        }
                    }
                }
            }

            @Override
            public void previewImage(ArrayList<String> imageList, int selectPosition) {

            }
        });

        if (savedInstanceState != null) {
            mSelectedImgList = (ArrayList<ImageModel>) savedInstanceState.getSerializable("selected_img_list");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_STORE:
                    if (resultCode == RESULT_OK && data != null) {
                        mResults = data.getStringArrayListExtra("image_paths");
                    }
                    if (mResults.size() > 0) {
                        for (int i = 0; i < mResults.size(); i++) {
                            if (mSelectedImgList.size() < 9) {
                                mSelectedImgList.add(new ImageModel(Uri.fromFile(new File(mResults.get(i))).toString()));
                            }
                        }
                        addImageListAdapter.notifyDataSetChanged();//添加
                    }
                    break;
                case CAMERA_REQUEST_CODE:
                    File newFile = new File(imagePath, mRandomPicName);
                    if (newFile.exists()) {
                        Uri contentUri = FileProvider.getUriForFile(this, APP_FILE_PROVIDER, newFile);
                        if (contentUri != null) {
                            mSelectedImgList.add(new ImageModel(contentUri.toString()));
                            addImageListAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
