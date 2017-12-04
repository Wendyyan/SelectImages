package com.zyf.selectimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyf.selectimage.R;
import com.zyf.selectimage.model.PhotoAlbumModel;
import com.zyf.selectimage.util.SDCardImageLoader;
import com.zyf.selectimage.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zyf on 2017/12/4.
 */

public class PhotoAlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PhotoAlbumModel> list;

    private SDCardImageLoader loader;

    public PhotoAlbumAdapter(Context context, ArrayList<PhotoAlbumModel> list) {
        this.context = context;
        this.list = list;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_album, null);
            holder = new ViewHolder();
            holder.ivAlbum = (ImageView)convertView.findViewById(R.id.iv_album);
            holder.tvAlbumName = (TextView)convertView.findViewById(R.id.tv_album_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //图片（缩略图）
        String filePath = list.get(position).getFirstImagePath();
        holder.ivAlbum.setTag(filePath);
        if (filePath != null) {
            loader.loadImage(4, filePath, holder.ivAlbum);
        }
        //文字
        holder.tvAlbumName.setText(getPathNameToShow(list.get(position)));

        return convertView;
    }

    static class ViewHolder {
        ImageView ivAlbum;
        TextView tvAlbumName;
    }

    /**
     * 根据完整路径，获取最后一级路径，并拼上文件数用以显示。
     */
    private String getPathNameToShow(PhotoAlbumModel item) {
        String absolutePath = item.getPathName();
        int lastSeparator = absolutePath.lastIndexOf(File.separator);
        return absolutePath.substring(lastSeparator + 1);
    }
}
