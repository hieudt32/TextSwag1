package com.example.baotu.textswag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.baotu.textswag.R;
import com.example.baotu.textswag.utility.Measure;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<String> listImage;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public GalleryAdapter() {
    }

    public GalleryAdapter(ArrayList<String> listImage, Context mContext) {
        this.listImage = listImage;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        int width = (mContext.getResources().getDisplayMetrics().widthPixels - 3 * Measure.pxToDp(3, mContext)) / 3;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.itemView.setLayoutParams(params);
        Glide.with(mContext).load(listImage.get(position)).into(holder.getImvGallery());

    }

    @Override
    public int getItemCount() {
        return (listImage != null) ? listImage.size() : 0;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imv_Gallery;

        public ImageView getImvGallery() {
            return imv_Gallery;
        }

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imv_Gallery = (ImageView) itemView.findViewById(R.id.imv_gallery);
        }
    }
}
