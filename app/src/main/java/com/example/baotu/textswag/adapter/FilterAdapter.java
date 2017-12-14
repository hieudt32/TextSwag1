package com.example.baotu.textswag.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.activity.EditActivity;
import com.example.baotu.textswag.model.Filter;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private Context mContext;
    private EditActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Filter> mListFilter;

    public FilterAdapter() {
    }

    public FilterAdapter(Context mContext, EditActivity mActivity, ArrayList<Filter> mListFilter) {
        this.mContext = mContext;
        this.mActivity=mActivity;
        this.mListFilter = mListFilter;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new FilterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, final int position) {
        final Filter filter = mListFilter.get(position);
        if (filter.getBChoosed()) {
            holder.getV_StatusChoose().setBackgroundResource(R.drawable.item_list_choose);
        } else {
            holder.getV_StatusChoose().setBackgroundResource(R.color.text_swag_transparent);
        }
        holder.getImv_Filter().setImageBitmap(filter.getIconImage());
        holder.getV_StatusChoose().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onFilterClick(position,mListFilter.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListFilter != null) ? mListFilter.size() : 0;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imv_Filter;
        private View v_StatusChoose;

        public FilterViewHolder(View itemView) {
            super(itemView);
            imv_Filter = (ImageView) itemView.findViewById(R.id.imv_list);
            v_StatusChoose = itemView.findViewById(R.id.v_status_choose);
        }


        public ImageView getImv_Filter() {
            return imv_Filter;
        }

        public void setImv_Filter(ImageView imv_Filter) {
            this.imv_Filter = imv_Filter;
        }

        public View getV_StatusChoose() {
            return v_StatusChoose;
        }

        public void setV_StatusChoose(View v_StatusChoose) {
            this.v_StatusChoose = v_StatusChoose;
        }
    }
}
