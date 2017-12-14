package com.example.baotu.textswag.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.baotu.textswag.implement.OnSaveImageCompleteListener;
import com.example.baotu.textswag.view.ProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SaveImageTask extends AsyncTask<Bitmap, Void, Void> {

    private String pathImage = null;
    private Bitmap mBitmap;
    private OnSaveImageCompleteListener mOnSaveImageCompleteListener;
    private ProgressDialog mDialog;
    private Context mContext;

    public OnSaveImageCompleteListener getmOnSaveImageCompleteListener() {
        return mOnSaveImageCompleteListener;
    }

    public void setmOnSaveImageCompleteListener(OnSaveImageCompleteListener mOnSaveImageCompleteListener) {
        this.mOnSaveImageCompleteListener = mOnSaveImageCompleteListener;
    }

    public SaveImageTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Bitmap... params) {

        mBitmap = params[0];
        FileOutputStream out = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File mydir = new File(root + File.separator + "TextSwag");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        String fileName = "TextSwag-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        File file = new File(mydir, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            pathImage = file.getPath();
        } catch (Exception e) {
            pathImage = null;
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Saving...");
        mDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
        if (mOnSaveImageCompleteListener != null) {
            mOnSaveImageCompleteListener.onSaveImageComplete(pathImage);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }
}
