package com.zyf.selectimage.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.zyf.selectimage.R;
import com.zyf.selectimage.adapter.PhotoAlbumAdapter;
import com.zyf.selectimage.adapter.PhotoWallAdapter;
import com.zyf.selectimage.model.PhotoAlbumModel;
import com.zyf.selectimage.model.PhotoWallModel;
import com.zyf.selectimage.util.ScreenUtils;
import com.zyf.selectimage.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class PhotoAlbumActivity extends AppCompatActivity {

    private ListView lvAlbum;

    private int imageNum;
    private static final int IMAGE_SELECTOR = 310;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        lvAlbum = (ListView)findViewById(R.id.lv_album);
        imageNum = getIntent().getIntExtra("selected_img_num", 0);

        //获取屏幕像素
        ScreenUtils.initScreen(this);
        if (!Utility.isSDcardOK()) {
            Utility.showToast(this, "SD卡不可用。");
            return;
        }
        //使用ContentProvider
        final ArrayList<PhotoAlbumModel> list = new ArrayList<>();
        //相册
        if (getImagePathsByContentProvider() != null && getImagePathsByContentProvider().size() > 0) {
            for (PhotoAlbumModel model : getImagePathsByContentProvider()) {
                if (model.getFileCount() > 0) {
                    list.add(model);
                }
            }
        }

        PhotoAlbumAdapter adapter = new PhotoAlbumAdapter(this, list);
        lvAlbum.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoAlbumActivity.this, PhotoWallActivity.class);
                intent.putExtra("folder_path", list.get(position).getPathName());
                intent.putExtra("selected_img_num", imageNum);
                startActivityForResult(intent, IMAGE_SELECTOR);
            }
        });
    }

    /**
     * 获取目录中图片的个数。
     */
    private int getImageCount(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        for (File file : files) {
            if (Utility.isImage(file.getName())) {
                count++;
            }
        }

        return count;
    }

    /**
     * 获取目录中最新的一张图片的绝对路径。
     */
    private String getFirstImagePath(File folder) {
        File[] files = folder.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            File file = files[i];
            if (Utility.isImage(file.getName())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    /**
     * 使用ContentProvider读取SD卡所有图片。
     */
    private ArrayList<PhotoAlbumModel> getImagePathsByContentProvider() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<PhotoAlbumModel> list = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                //路径缓存，防止多次扫描同一目录
                HashSet<String> cachePath = new HashSet<String>();
                list = new ArrayList<PhotoAlbumModel>();

                while (true) {
                    // 获取图片的路径
                    String imagePath = cursor.getString(0);

                    File parentFile = new File(imagePath).getParentFile();
                    String parentPath = parentFile.getAbsolutePath();

                    //不扫描重复路径
                    if (!cachePath.contains(parentPath)) {
                        list.add(new PhotoAlbumModel(parentPath, getImageCount(parentFile),
                                getFirstImagePath(parentFile)));
                        cachePath.add(parentPath);
                    }

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }

            cursor.close();
        }

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_SELECTOR:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> paths = data.getStringArrayListExtra("image_paths");
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("image_paths", paths);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (resultCode == RESULT_FIRST_USER) {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
