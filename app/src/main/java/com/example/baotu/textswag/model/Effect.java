package com.example.baotu.textswag.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;


public class Effect {
    @SerializedName("EffectName")
    private String mEffectName;

    public String getmEffectName() {
        return mEffectName;
    }

    public void setmEffectName(String mEffectName) {
        this.mEffectName = mEffectName;
    }

    private Bitmap mThumbnail;

    public Bitmap getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(Bitmap mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    private boolean mBChoosed = false;

    public boolean getBChoosed() {
        return mBChoosed;
    }

    public void setBChoosed(boolean isChoosed) {
        mBChoosed = isChoosed;
    }
}
