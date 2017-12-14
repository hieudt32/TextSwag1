package com.example.baotu.textswag.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.model.Font;

import java.util.ArrayList;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontHolder> {

    private Context mContex;
    private LayoutInflater layoutInflater;
    private ArrayList<Font> mListFont;

    public FontAdapter(Context mContex, ArrayList<Font> mListFont) {
        this.mContex = mContex;
        this.layoutInflater = LayoutInflater.from(mContex);
        this.mListFont = mListFont;
    }


    @Override
    public FontHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_font, parent, false);
        return new FontHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FontHolder holder, int position) {
        Font font = mListFont.get(position);
        holder.getTv_Font().setText("Text Swag");
        holder.getTv_Font().setTypeface(Typeface.createFromAsset(mContex.getAssets(), "font/" + font.getName()));
        if (font.isSelectMode()) {
            holder.getTv_Font().setBackgroundColor(mContex.getResources().getColor(R.color.text_swag_accent_70));
        } else {
            holder.getTv_Font().setBackgroundColor(mContex.getResources().getColor(R.color.text_swag_transparent));
        }
    }


    @Override
    public int getItemCount() {
        return mListFont != null ? mListFont.size() : 0;
    }

    static class FontHolder extends RecyclerView.ViewHolder {
        private TextView tv_Font;

        public TextView getTv_Font() {
            return tv_Font;
        }

        public void setTv_Font(TextView tv_Font) {
            this.tv_Font = tv_Font;
        }

        public FontHolder(View itemView) {
            super(itemView);
            tv_Font = (TextView) itemView.findViewById(R.id.tv_font);
        }
    }
}
