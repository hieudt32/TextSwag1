package com.example.baotu.textswag.task;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.baotu.textswag.activity.EditActivity;
import com.example.baotu.textswag.model.Filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;


public class GetFilterThumbnailTask extends AsyncTask<Void, Void, ArrayList<Filter>> {

    private Bitmap mBitmap;
    private Context mContext;
    private Activity mActivity;
    private Uri mUri;
    private ArrayList<Filter> mListFilter;

    public GetFilterThumbnailTask(Context mContext, Activity mActivity, Uri mUri, ArrayList<Filter> mListFilter) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mUri = mUri;
        this.mListFilter = mListFilter;
    }

    @Override
    protected ArrayList<Filter> doInBackground(Void... params) {
        mBitmap = BitmapFactory.decodeFile(mUri.getPath());
        Bitmap thumb = ThumbnailUtils.extractThumbnail(mBitmap, 256, 256);
        for (int i = 0; i < mListFilter.size(); i++) {
            Filter filter = mListFilter.get(i);
            GPUImage thumbView = new GPUImage(mContext);
            InputStream iStream = null;
            GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
            try {
                String key = "filters/" + "cat_01/" + filter.getFilterId() + ".acv";
                iStream = mContext.getAssets().open(key);
                toneCurveFilter.setFromCurveFileInputStream(iStream);
                thumbView.setFilter(toneCurveFilter);

                Bitmap bitmap = thumbView.getBitmapWithFilterApplied(thumb);
                mListFilter.get(i).setKey(key);
                mListFilter.get(i).setIconImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return mListFilter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Filter> filters) {
        super.onPostExecute(filters);
        ((EditActivity) mActivity).showFilterThumbs(mListFilter);
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
