package com.example.baotu.textswag.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baotu.textswag.R;


public class ProgressDialog extends Dialog {
    private Animation animation;
    private ImageView mImageProgressView;
    private TextView mTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterContentView();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        afterContentView();
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterContentView();
    }

    private void afterContentView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = -1;
        getWindow().setAttributes(lp);
        this.mImageProgressView = (ImageView) findViewById(R.id.progress);
        this.mTextView = (TextView) findViewById(R.id.message);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_loading);
    }

    public ProgressDialog(Context context) {
        super(context, R.style.BaseDialog);
        setContentView(R.layout.dialog_progress);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, R.style.BaseDialog);
        setContentView(R.layout.dialog_progress);
    }

    public ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.dialog_progress);
    }

    public void show() {
        if (this.animation != null) {
            mImageProgressView.startAnimation(animation);
        }
        super.show();
    }

    public void hide() {
        if (this.animation != null) {
            animation.cancel();
        }
        super.hide();
    }

    public void dismiss() {
        if (this.animation != null) {
            animation.cancel();
        }
        super.dismiss();
    }

    public void setMessage(String title) {
        if (this.mTextView != null) {
            this.mTextView.setText(title);
        }
    }
}
