package com.example.baotu.textswag.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.baotu.textswag.R;
import com.example.baotu.textswag.activity.EditActivity;
import com.example.baotu.textswag.model.Effect;

import java.util.ArrayList;


public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.EffectViewHolder> {

    private Context mContext;
    private EditActivity mActivity;
    private ArrayList<Effect> mListEffect;
    private LayoutInflater layoutInflater;

    public EffectAdapter(Context mContext, EditActivity mActivity, ArrayList<Effect> mListEffect) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mListEffect = mListEffect;
        layoutInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public EffectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_list, parent, false);
        return new EffectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EffectViewHolder holder, int position) {
        final Effect effect = mListEffect.get(position);
        if (effect.getBChoosed()) {
            holder.getV_StatusChoose().setBackgroundResource(R.drawable.item_list_choose);
        } else {
            holder.getV_StatusChoose().setBackgroundResource(R.color.text_swag_transparent);
        }
        String s = "file:///android_asset/effect/cat_00/" + effect.getmEffectName() + ".jpg";
        Glide.with(mActivity).load(s).into(holder.getImv_Effect());
        holder.getV_StatusChoose().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onApplyEffect(effect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListEffect != null) ? mListEffect.size() : 0;
    }

    public class EffectViewHolder extends RecyclerView.ViewHolder {
        private ImageView imv_Effect;
        private View v_StatusChoose;

        public EffectViewHolder(View itemView) {
            super(itemView);
            imv_Effect = (ImageView) itemView.findViewById(R.id.imv_list);
            v_StatusChoose = itemView.findViewById(R.id.v_status_choose);
        }


        public ImageView getImv_Effect() {
            return imv_Effect;
        }

        public void setImv_Effect(ImageView imv_Filter) {
            this.imv_Effect = imv_Filter;
        }

        public View getV_StatusChoose() {
            return v_StatusChoose;
        }

        public void setV_StatusChoose(View v_StatusChoose) {
            this.v_StatusChoose = v_StatusChoose;
        }
    }
}
