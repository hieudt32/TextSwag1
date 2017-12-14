package com.example.baotu.textswag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.adapter.EffectAdapter;
import com.example.baotu.textswag.adapter.FilterAdapter;
import com.example.baotu.textswag.adapter.FontAdapter;
import com.example.baotu.textswag.implement.OnItemRecyclerViewClickListener;
import com.example.baotu.textswag.implement.OnSaveImageCompleteListener;
import com.example.baotu.textswag.model.Effect;
import com.example.baotu.textswag.model.Filter;
import com.example.baotu.textswag.model.Font;
import com.example.baotu.textswag.other.Defines;
import com.example.baotu.textswag.task.ApplyFilterTask;
import com.example.baotu.textswag.task.GetFilterThumbnailTask;
import com.example.baotu.textswag.task.SaveImageTask;
import com.example.baotu.textswag.utility.OnItemRecyclerViewTouchListener;
import com.example.baotu.textswag.view.ProgressDialog;
import com.larswerkman.holocolorpicker.ColorPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class EditActivity extends BaseActivity implements View.OnClickListener, ColorPicker.OnColorChangedListener,
        ColorPicker.OnColorSelectedListener, OnSaveImageCompleteListener {

    private TextView tv_Label;
    private ImageButton imb_Home, imb_Done, imb_Edit, imb_Gift, imb_BackFilter, imb_Write, imb_HideFont;
    private GPUImageView mGPUImage;
    private ImageView imv_Preview;
    private SeekBar sb_Alpha, sb_Zoom;
    private RelativeLayout rl_Bottom, rl_Tool, rl_Font, rl_Preview;
    private View v_Bottom;
    private ImageButton imb_Style, imb_Font, imb_Filter, imb_Effect;
    private ImageButton imb_ChooseColor;
    private RecyclerView rcv_List;
    private RecyclerView rcv_Font;
    private ArrayList<Font> listFont;
    private ArrayList<Filter> listFilter = new ArrayList<>();
    private ArrayList<Effect> listEffect = new ArrayList<>();
    private FontAdapter fontAdapter;
    private FilterAdapter filterAdapter;
    private EffectAdapter effectAdapter;
    private Uri uri;

    private ColorPicker colorPicker;

    private int mFontSelected = 0;

    private ProgressDialog mProgressDialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initVariables() {
        tv_Label = (TextView) findViewById(R.id.tv_label_edit);
        imb_Home = (ImageButton) findViewById(R.id.imb_home_edit);
        imb_Done = (ImageButton) findViewById(R.id.imb_done_edit);
        imb_Edit = (ImageButton) findViewById(R.id.imb_edit);
        imb_Gift = (ImageButton) findViewById(R.id.imb_gift);
        mGPUImage = (GPUImageView) findViewById(R.id.gpuimage);
        imv_Preview = (ImageView) findViewById(R.id.imv_preview_edit);
        sb_Alpha = (SeekBar) findViewById(R.id.sb_alpha);
        sb_Zoom = (SeekBar) findViewById(R.id.sb_zoom);
        imb_BackFilter = (ImageButton) findViewById(R.id.imb_back_filter);
        imb_HideFont = (ImageButton) findViewById(R.id.imb_hide_font);
        imb_Write = (ImageButton) findViewById(R.id.imb_write);
        rl_Preview = (RelativeLayout) findViewById(R.id.rl_image_edit);
        rl_Bottom = (RelativeLayout) findViewById(R.id.rl_bottom_edit);
        v_Bottom = findViewById(R.id.v_bottom_edit);
        rl_Tool = (RelativeLayout) findViewById(R.id.rl_tool_edit);
        rl_Font = (RelativeLayout) findViewById(R.id.rl_font);
        imb_Style = (ImageButton) findViewById(R.id.imb_style);
        imb_Filter = (ImageButton) findViewById(R.id.imb_filter);
        imb_Font = (ImageButton) findViewById(R.id.imb_font);
        imb_Effect = (ImageButton) findViewById(R.id.imb_effect);
        imb_ChooseColor = (ImageButton) findViewById(R.id.imb_choose_color);

        rcv_List = (RecyclerView) findViewById(R.id.rcv_list);
        rcv_Font = (RecyclerView) findViewById(R.id.rcv_font);

        colorPicker = (ColorPicker) findViewById(R.id.picker);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        uri = intent.getData();
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//        mGPUImage.setImage(bitmap);
        imv_Preview.setImageBitmap(bitmap);

        imb_Home.setOnClickListener(this);
        imb_Done.setOnClickListener(this);
        imb_Gift.setOnClickListener(this);
        imb_Edit.setOnClickListener(this);
        imb_Style.setOnClickListener(this);
        imb_Effect.setOnClickListener(this);
        imb_Filter.setOnClickListener(this);
        imb_Font.setOnClickListener(this);

        imb_BackFilter.setOnClickListener(this);
        imb_HideFont.setOnClickListener(this);
        imb_Write.setOnClickListener(this);
        imb_ChooseColor.setOnClickListener(this);

//        colorPicker.setOnColorChangedListener(this);
        colorPicker.setOnColorSelectedListener(this);
        onSeekBarChange();

        setFontData();

    }


    private void onSeekBarChange() {
        sb_Zoom.setProgress(100);
        sb_Alpha.setProgress(100);
        sb_Zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "Zoom to:" + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_Alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), "Set Alpha:" + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void setFontData() {
        listFont = new ArrayList<>();
        for (int i = 0; i < Defines.LIST_FONT.length; i++) {
            Font font;
            if (i == 0) {
                font = new Font(i, Defines.LIST_FONT[i], true);
            } else {
                font = new Font(i, Defines.LIST_FONT[i], false);
            }
            listFont.add(font);
        }
        fontAdapter = new FontAdapter(this, listFont);

        rcv_Font.setAdapter(fontAdapter);
        rcv_Font.addOnItemTouchListener(new OnItemRecyclerViewTouchListener(this, new OnItemRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listFont.get(mFontSelected).setSelectMode(false);
                listFont.get(position).setSelectMode(true);
                mFontSelected = position;
                fontAdapter.notifyDataSetChanged();
            }
        }));
    }


    private void showEffect() {
        getListEffect();
        effectAdapter = new EffectAdapter(getApplicationContext(), this, listEffect);
        rcv_List.setAdapter(effectAdapter);
    }

    private void getListEffect() {
        InputStream inputStream = getResources().openRawResource(R.raw.effect);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
            JSONArray effect = jObject.getJSONArray("Effect");
            for (int i = 0; i < effect.length(); i++) {
                JSONObject image = effect.getJSONObject(i);
                String name = image.getString("EffectName");
                Effect temp = new Effect();
                temp.setmEffectName(name);
                temp.setBChoosed(false);
                listEffect.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onApplyEffect(Effect effect) {
        for (int i = 0; i < listEffect.size(); i++) {
            if (listEffect.get(i) == effect) {
                listEffect.get(i).setBChoosed(true);
            } else {
                listEffect.get(i).setBChoosed(false);
            }
        }
        effectAdapter.notifyDataSetChanged();
        InputStream iStream = null;
        GPUImageOverlayBlendFilter overlayBlendFilter = new GPUImageOverlayBlendFilter();
        try {
            String key = "effect/cat_00/" + effect.getmEffectName() + ".jpg";
            iStream = getApplicationContext().getAssets().open(key);
            overlayBlendFilter.setBitmap(BitmapFactory.decodeStream(iStream));
            initializeFilters(overlayBlendFilter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!imb_BackFilter.isShown()) imb_BackFilter.setVisibility(View.VISIBLE);
    }

    public void onApplyFilter(Filter filter) {
        for (int i = 0; i < listFilter.size(); i++) {
            if (listFilter.get(i) == filter) {
                listFilter.get(i).setBChoosed(true);
            } else {
                listFilter.get(i).setBChoosed(false);
            }
        }
        filterAdapter.notifyDataSetChanged();
        InputStream iStream = null;
        GPUImageToneCurveFilter toneCurveFilter = new GPUImageToneCurveFilter();
        try {
            iStream = getApplicationContext().getAssets().open(filter.getKey());
            toneCurveFilter.setFromCurveFileInputStream(iStream);
            initializeFilters(toneCurveFilter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeFilters(GPUImageFilter gpuImageFilter) {
        openProgressDialog("Applying...");
        new ApplyFilterTask(this, this, uri, gpuImageFilter).execute();
    }

    public void onCompleteApplyFilter(Bitmap bitmap) {
        closeProgressDialog();
//        mGPUImage.setImage(bitmap);
        imv_Preview.setImageBitmap(bitmap);
    }

    public void getListThumbnailFilter() {
        getListFilter();
        openProgressDialog("Loading...");
        new GetFilterThumbnailTask(getApplicationContext(), this, uri, listFilter).execute();
    }

    private void getListFilter() {
        InputStream inputStream = getResources().openRawResource(R.raw.filter);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
            JSONArray filter = jObject.getJSONArray("Filter");
            for (int i = 0; i < filter.length(); i++) {
                JSONObject image = filter.getJSONObject(i);
                String name = image.getString("FilterName");
                String id = image.getString("FilterId");
                Filter temp = new Filter();
                temp.setFilterName(name);
                temp.setFilterId(id);
                temp.setIconImage(null);
                temp.setFilterCategoryId(null);
                temp.setBChoosed(false);
                listFilter.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFilterThumbs(final ArrayList<Filter> list) {
        closeProgressDialog();
        filterAdapter = new FilterAdapter(this, this, list);
        rcv_List.setAdapter(filterAdapter);
    }

    public void onFilterClick(int position, int size) {
        if (position >= 0 && position < size) {
            onApplyFilter(listFilter.get(position));
            if (!imb_BackFilter.isShown()) imb_BackFilter.setVisibility(View.VISIBLE);
        }
    }

    private void openProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    private void onButtonBackFilterClick() {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//        mGPUImage.setImage(bitmap);
        imv_Preview.setImageBitmap(bitmap);
        if (imb_BackFilter.isShown()) imb_BackFilter.setVisibility(View.GONE);
        if (rcv_List.getAdapter() == filterAdapter) {
            for (int i = 0; i < listFilter.size(); i++) {
                listFilter.get(i).setBChoosed(false);
            }
            filterAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < listEffect.size(); i++) {
                listEffect.get(i).setBChoosed(false);
            }
            effectAdapter.notifyDataSetChanged();
        }
    }

    private void onButtonStyleClick() {
        if (!rl_Bottom.isShown()) {
            rl_Bottom.setVisibility(View.VISIBLE);
            tv_Label.setText("style");
        } else {
            rl_Bottom.setVisibility(View.GONE);
            tv_Label.setText(null);
        }
        if (imb_HideFont.isShown()) imb_HideFont.setVisibility(View.GONE);
        if (!v_Bottom.isShown()) v_Bottom.setVisibility(View.VISIBLE);
        rl_Tool.setVisibility(View.GONE);
        if (rl_Font.isShown()) rl_Font.setVisibility(View.GONE);
    }

    private void onButtonFontClick() {
        if (!rl_Font.isShown()) rl_Font.setVisibility(View.VISIBLE);
        if (!imb_HideFont.isShown()) imb_HideFont.setVisibility(View.VISIBLE);
        if (rl_Bottom.isShown()) rl_Bottom.setVisibility(View.GONE);
        if (v_Bottom.isShown()) v_Bottom.setVisibility(View.GONE);
        rl_Tool.setVisibility(View.GONE);
        tv_Label.setText("text");
    }

    private void onButtonFilterClick() {
        if (rl_Bottom.isShown()) {
            if (rcv_List.getAdapter() != filterAdapter) {
                if (!listFilter.isEmpty()) {
                    rcv_List.setAdapter(filterAdapter);
                } else {
                    getListThumbnailFilter();
                }
                tv_Label.setText("filter");
            } else {
                if (filterAdapter == null) {
                    getListThumbnailFilter();
                    tv_Label.setText("filter");
                } else {
                    rl_Bottom.setVisibility(View.GONE);
                    tv_Label.setText(null);
                }
            }
        } else {
            if (!listFilter.isEmpty()) {
                rcv_List.setAdapter(filterAdapter);
            } else {
                getListThumbnailFilter();
            }
            rl_Bottom.setVisibility(View.VISIBLE);
            tv_Label.setText("filter");
        }
        if (imb_HideFont.isShown()) imb_HideFont.setVisibility(View.GONE);
        if (rl_Font.isShown()) rl_Font.setVisibility(View.GONE);
        if (!v_Bottom.isShown()) v_Bottom.setVisibility(View.VISIBLE);
        rl_Tool.setVisibility(View.GONE);

    }

    private void onButtonEffectClick() {
        if (rl_Bottom.isShown()) {
            if (rcv_List.getAdapter() != effectAdapter) {
                if (!listEffect.isEmpty()) {
                    rcv_List.setAdapter(effectAdapter);
                } else {
                    showEffect();
                }
                tv_Label.setText("effect");
            } else {
                if (effectAdapter == null) {
                    showEffect();
                    tv_Label.setText("effect");
                } else {
                    rl_Bottom.setVisibility(View.GONE);
                    tv_Label.setText(null);
                }
            }
        } else {
            if (!listEffect.isEmpty()) {
                rcv_List.setAdapter(effectAdapter);
            } else {
                showEffect();
            }
            rl_Bottom.setVisibility(View.VISIBLE);
            tv_Label.setText("effect");
        }
        if (imb_HideFont.isShown()) imb_HideFont.setVisibility(View.GONE);
        if (rl_Font.isShown()) rl_Font.setVisibility(View.GONE);
        if (!v_Bottom.isShown()) v_Bottom.setVisibility(View.VISIBLE);
        rl_Tool.setVisibility(View.GONE);

    }

    private Bitmap captureBitmapOfViews(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true).copy(
                Bitmap.Config.ARGB_8888, false);
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    private void startShareActivity(String path) {
        Intent intent = new Intent(EditActivity.this, ShareActivity.class);
        intent.putExtra("URI_IMAGE", "file://" + path);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_home_edit:
                onBackPressed();
                break;
            case R.id.imb_done_edit:
                SaveImageTask saveImageTask = new SaveImageTask(this);
                saveImageTask.setmOnSaveImageCompleteListener(this);
                saveImageTask.execute(captureBitmapOfViews(rl_Preview));
                break;
            case R.id.imb_edit:
                if (!rl_Tool.isShown()) {
                    rl_Tool.setVisibility(View.VISIBLE);
                } else {
                    rl_Tool.setVisibility(View.GONE);
                }
                break;
            case R.id.imb_gift:
                break;
            case R.id.imb_style:
                onButtonStyleClick();
                break;
            case R.id.imb_font:
                onButtonFontClick();
                break;
            case R.id.imb_filter:
                onButtonFilterClick();
                break;
            case R.id.imb_effect:
                onButtonEffectClick();
                break;
            case R.id.imb_choose_color:
                if (colorPicker.isShown()) {
                    colorPicker.setVisibility(View.GONE);
                } else {
                    colorPicker.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imb_back_filter:
                onButtonBackFilterClick();
                break;
            case R.id.imb_write:
                Intent intent = new Intent(this, InputActivity.class);
                startActivityForResult(intent, Defines.REQUEST_INPUT_TEXT);
                break;
            case R.id.imb_hide_font:
                if (rl_Font.isShown()) {
                    rl_Font.setVisibility(View.GONE);
                    imb_HideFont.setVisibility(View.GONE);
                }
                if (rl_Tool.isShown()) rl_Tool.setVisibility(View.GONE);
                tv_Label.setText(null);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Defines.REQUEST_INPUT_TEXT) {
            String text = data.getData().toString();
            Log.d("input", text);
        }
    }

    @Override
    public void onColorChanged(int color) {
        Toast.makeText(this, "Color Changed: " + color, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onColorSelected(int color) {
        Toast.makeText(this, "Color Selected: " + color, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveImageComplete(String path) {
        if (path != null) {
            startShareActivity(path);
        }
    }
}
