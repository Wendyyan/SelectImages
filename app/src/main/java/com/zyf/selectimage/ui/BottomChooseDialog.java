package com.zyf.selectimage.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyf.selectimage.R;

/**
 * Created by zyf on 2017/12/1.
 */

public class BottomChooseDialog extends Dialog {

    int layoutRes;// 布局文件
    Context context;

    private BottomDialogListener mListener;
    private  boolean fillWidth=false;
    //弹出框（相机，相册，取消按钮）
    private TextView mtv_choose1, mtv_choose2, mTv_cancel;
    String choose1;
    String choose2;
    public BottomChooseDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义布局的构造方法
     *
     * @param context
     * @param resLayout
     */
    public BottomChooseDialog(Context context, int resLayout, BottomDialogListener listener) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;
        mListener = listener;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */
    public BottomChooseDialog(Context context, int theme, int resLayout, BottomDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
        mListener = listener;
    }
    public BottomChooseDialog(Context context, int theme, int resLayout, String choose1, String choose2, BottomDialogListener listener, boolean fillWidth) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
        mListener = listener;
        this.fillWidth=fillWidth;
        this.choose1=choose1;
        this.choose2=choose2;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
        if(fillWidth)
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//宽度和屏幕相等
        initView();
        initLayout();
        mtv_choose1.setText(choose1);
        mtv_choose2.setText(choose2);
    }

    //加载布局上的控件
    private void initView() {
        mtv_choose1 = (TextView) findViewById(R.id.tv_bottom_choose1);
        mtv_choose2 = (TextView) findViewById(R.id.tv_bottom_choose2);
        mTv_cancel = (TextView) findViewById(R.id.tv_bottom_cancel);
        mtv_choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChoose1(v);
                BottomChooseDialog.this.dismiss();
            }
        });
        mtv_choose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChoose2(v);
                BottomChooseDialog.this.dismiss();
            }
        });
        mTv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomChooseDialog.this.dismiss();
            }
        });
    }

    //弹出框控件点击监听器
    public interface BottomDialogListener {
        public void onChoose1(View v);
        public void onChoose2(View v);
    }

    /**
     * 初始化布局
     */
    public void initLayout()
    {
        WindowManager.LayoutParams localLayoutParams = this
                .getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM;
        localLayoutParams.y = 0;
        this.onWindowAttributesChanged(localLayoutParams);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

}
