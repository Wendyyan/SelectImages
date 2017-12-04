package com.zyf.selectimage.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zyf.selectimage.R;
import com.zyf.selectimage.model.ImageModel;

import java.util.ArrayList;

/**
 * Created by zyf on 2017/12/4.
 */

public class AddImageListAdapter extends BaseAdapter {

    int maxCount = 9;//最多上传9张
    private ArrayList<ImageModel> mData = new ArrayList<>();
    private Context mContext;
    public AddImagesListener addImagesListener;

    public AddImageListAdapter(ArrayList<ImageModel> data, Context context) {
        mData = data;
        mContext = context;
    }

    public ArrayList<ImageModel> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        if (mData.size() < maxCount)
            return mData.size() + 1;
        else
            return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if (mData.size() > position)
            return mData.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_upload_image, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.sdvPreview = (SimpleDraweeView) convertView.findViewById(R.id.sdv_preview);
            viewHolder.sdvAddImage = (SimpleDraweeView) convertView.findViewById(R.id.sdv_add_img);
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            viewHolder.sdvPreview.setAspectRatio(1f);
            viewHolder.sdvAddImage.setAspectRatio(1f);
            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(v.getTag().toString());
                    if (pos < mData.size()) {
                        if (addImagesListener != null)
                            addImagesListener.deleteImage(mData.get(pos).imgUrl);
                        mData.remove(pos);
                        notifyDataSetChanged();//删除
                    }
                }
            });

            viewHolder.sdvAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(v.getTag().toString());
                    if (pos <= mData.size()) {
                        if (addImagesListener != null)
                            addImagesListener.addImage();
                    }
                }
            });

            viewHolder.sdvPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(v.getTag().toString());
                    if (pos < mData.size()) {
                        if (addImagesListener != null)
                            addImagesListener.previewImage(mContext, pos);
                    }
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.sdvPreview.setTag(position);
        viewHolder.sdvAddImage.setTag(position);
        viewHolder.ivDelete.setTag(position);
        if (getItem(position) != null) {
            viewHolder.sdvPreview.setImageURI(Uri.parse(mData.get(position).imgUrl));
            viewHolder.sdvPreview.setVisibility(View.VISIBLE);
            viewHolder.sdvAddImage.setVisibility(View.GONE);
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
        } else {//最后一项为添加图片按钮键
            viewHolder.sdvPreview.setVisibility(View.GONE);
            viewHolder.sdvAddImage.setVisibility(View.VISIBLE);
            viewHolder.ivDelete.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        SimpleDraweeView sdvPreview;
        SimpleDraweeView sdvAddImage;
        ImageView ivDelete;
    }

    public interface AddImagesListener{

        void addImage();

        void deleteImage(String uriPath);

        void previewImage(Context context, int position);
    }
}
