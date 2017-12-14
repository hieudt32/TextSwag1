package com.example.baotu.textswag.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.baotu.textswag.activity.EditActivity;

import java.io.File;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


public class ApplyFilterTask extends AsyncTask<Void, Void, Bitmap> {

    private Context mContext;
    private EditActivity mActivity;
    private Uri mUri;
    private GPUImageFilter mFilter;

    public ApplyFilterTask(Context mContext, EditActivity mActivity, Uri mUri, GPUImageFilter mFilter) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mUri = mUri;
        this.mFilter = mFilter;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = BitmapFactory.decodeFile(mUri.getPath());
        GPUImage thumbView = new GPUImage(mContext);
        thumbView.setFilter(mFilter);
        return thumbView.getBitmapWithFilterApplied(bitmap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mActivity.onCompleteApplyFilter(bitmap);
        deleteCacheFolder();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private void deleteCacheFolder() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "Cache");
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                new File(dir, file).delete();
            }
        }
    }
}
