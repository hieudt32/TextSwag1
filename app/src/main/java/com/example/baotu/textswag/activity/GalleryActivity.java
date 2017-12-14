package com.example.baotu.textswag.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.adapter.GalleryAdapter;
import com.example.baotu.textswag.implement.OnItemRecyclerViewClickListener;
import com.example.baotu.textswag.utility.GridSpacingItemDecoration;
import com.example.baotu.textswag.utility.Measure;
import com.example.baotu.textswag.utility.OnItemRecyclerViewTouchListener;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends BaseActivity {

    private RecyclerView rcv_Gallery;
    private GalleryAdapter mAdapter;
    private ArrayList<String> mListImage;
    private ImageButton imb_Home;
    private File mFile;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initVariables() {
        rcv_Gallery = (RecyclerView) findViewById(R.id.rcv_gallery);
        imb_Home = (ImageButton) findViewById(R.id.imb_home_gallery);

    }

    @Override
    protected void initData() {
        imb_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getListFiles();
        mAdapter = new GalleryAdapter(mListImage, this);
        rcv_Gallery.setAdapter(mAdapter);
        rcv_Gallery.addItemDecoration(new GridSpacingItemDecoration(3, Measure.pxToDp(3, getApplicationContext()), true));
        rcv_Gallery.setLayoutManager(new GridLayoutManager(this, 3));

        rcv_Gallery.addOnItemTouchListener(new OnItemRecyclerViewTouchListener(this, new OnItemRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(GalleryActivity.this, ShareActivity.class);
                intent.putExtra("URI_IMAGE", "file://" + mListImage.get(position));
                intent.putExtra("LIST_IMAGE", mListImage);
                intent.putExtra("POSITION",position);
                intent.putExtra("parent_activity", "1");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }));
        mAdapter.notifyDataSetChanged();
    }

    private void getListFiles() {

        mListImage = new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mFile = getDir("TextSwag", Context.MODE_PRIVATE);
            if (!mFile.exists()) {
                mFile.mkdirs();
            }

        } else {
            mFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "TextSwag");
            mFile.mkdirs();
        }

        if (mFile.isDirectory() && mFile != null) {
            File[] listFile = mFile.listFiles();
            if (listFile != null) {
                for (File aListFile : listFile) {
                    mListImage.add(aListFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
