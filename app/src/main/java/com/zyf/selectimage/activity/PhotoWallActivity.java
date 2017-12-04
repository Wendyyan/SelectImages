package com.zyf.selectimage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyf.selectimage.R;
import com.zyf.selectimage.adapter.PhotoWallAdapter;
import com.zyf.selectimage.model.PhotoWallModel;
import com.zyf.selectimage.util.Utility;

import java.io.File;
import java.util.ArrayList;

public class PhotoWallActivity extends BaseActivity {

    private GridView photoWallGrid;
    private TextView tvFinish;

    private int maxCount = 9;//最多上传9张
    private ArrayList<PhotoWallModel> photoWallList = new ArrayList<>();
    private PhotoWallAdapter adapter;
    private int num;
    private String folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);
        photoWallGrid = (GridView)findViewById(R.id.photo_wall_grid);
        tvFinish = (TextView)findViewById(R.id.tv_finish);

        num = getIntent().getIntExtra("selected_img_num", 0);
        folderPath = getIntent().getStringExtra("folder_path");

        adapter = new PhotoWallAdapter(this, photoWallList);
        photoWallGrid.setAdapter(adapter);
        adapter.setOnChooseListener(new PhotoWallAdapter.OnChooseListener() {
            @Override
            public void onChooseClick(int position) {

                if (photoWallList.get(position).isCheck) {
                    photoWallList.get(position).isCheck = false;
                } else {
                    if (num + getSelectImagePaths().size() >= maxCount) {
                        Toast.makeText(PhotoWallActivity.this, "最多上传" + maxCount + "张照片",Toast.LENGTH_SHORT).show();
                    } else {
                        photoWallList.get(position).isCheck = true;
                    }
                }
                adapter.notifyDataSetChanged();

                if (getSelectImagePaths().size() > 0) {
                    tvFinish.setBackgroundResource(R.drawable.shape_rounded_blue);
                    tvFinish.setText(getString(R.string.info_finish) + "(" + getSelectImagePaths().size() + ")");
                    tvFinish.setEnabled(true);
                } else {
                    tvFinish.setBackgroundResource(R.drawable.shape_rounded_btn_gray);
                    tvFinish.setText(getString(R.string.info_finish));
                    tvFinish.setEnabled(false);
                }
            }
        });

        if (folderPath != null) {
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            ArrayList<String> list = new ArrayList<>();
            String folderName = folderPath.substring(lastSeparator + 1);
            getSupportActionBar().setTitle(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
            for (String path : list) {
                PhotoWallModel model = new PhotoWallModel(path, false);
                photoWallList.add(model);
            }
            adapter.notifyDataSetChanged();
        }

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectImagePaths().size() > 0) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("image_paths", getSelectImagePaths());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 获取指定路径下的所有图片文件。
     */
    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Utility.isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }

    //获取已选择的图片路径
    private ArrayList<String> getSelectImagePaths() {
        ArrayList<String> selectedImageList = new ArrayList<String>();
        for (int i = 0; i < photoWallList.size(); i++) {
            if (photoWallList.get(i).isCheck) {
                selectedImageList.add(photoWallList.get(i).path);
            }
        }
        return selectedImageList;
    }

}
