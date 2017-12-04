package com.zyf.selectimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zyf.selectimage.R;
import com.zyf.selectimage.model.PhotoWallModel;
import com.zyf.selectimage.ui.SquareLayout;
import com.zyf.selectimage.util.SDCardImageLoader;
import com.zyf.selectimage.util.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by zyf on 2017/12/4.
 */

public class PhotoWallAdapter extends BaseAdapter {
    private ArrayList<PhotoWallModel> mData;
    private Context mContext;
    private OnChooseListener chooseListener;
    private SDCardImageLoader loader;

    public PhotoWallAdapter(Context context, ArrayList<PhotoWallModel> dataList) {
        mContext = context;
        mData = dataList;

        loader = new SDCardImageLoader(ScreenUtils.getScreenW(), ScreenUtils.getScreenH());
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);//当前行是否可以点击
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_photo_wall, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
            viewHolder.slPhoto = (SquareLayout)convertView.findViewById(R.id.sl_photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.slPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseListener != null) {
                    chooseListener.onChooseClick(position);
                }
            }
        });

        if (mData.get(position).isCheck) {
            viewHolder.ivCheck.setImageResource(R.drawable.btn_pay_select);
        } else {
            viewHolder.ivCheck.setImageResource(R.drawable.btn_pay_select_gray);
        }
        String filePath = mData.get(position).getPath();
        viewHolder.ivPhoto.setTag(filePath);
        loader.loadImage(4, filePath, viewHolder.ivPhoto);
        return convertView;
    }


    class ViewHolder {
        ImageView ivPhoto;
        ImageView ivCheck;
        SquareLayout slPhoto;
    }

    public void setOnChooseListener(OnChooseListener chooseListener) {
        this.chooseListener = chooseListener;
    }

    public interface OnChooseListener {
        void onChooseClick(int position);
    }
}
