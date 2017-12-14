package com.example.baotu.textswag.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.cropper.CropImage;
import com.example.baotu.textswag.cropper.CropImageOptions;
import com.example.baotu.textswag.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

/**
 * Created by baotu on 7/3/2017.
 */

public class CropActivity extends BaseActivity implements View.OnClickListener,
        CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    /**
     * The crop image view library widget used in the activity
     */
    private CropImageView mCropImageView;

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private Uri mCropImageUri;

    /**
     * the options that were set for the crop image
     */
    private CropImageOptions mOptions;

    /**
     *
     */

    private ImageButton imb_Home, imb_Done;
    private ImageButton imb_RotateLeft, imb_RotateRight, imb_FlipVertical, imb_FlipHorizontal;
    private RadioButton rbt_RatioFree, rbt_RatioSquare, rbt_Ratio34, rbt_Ratio43, rbt_Ratio916,
            rbt_Ratio169;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_crop;
    }

    @Override
    protected void initVariables() {
        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);

        imb_Home = (ImageButton) findViewById(R.id.imb_home_crop);
        imb_Done = (ImageButton) findViewById(R.id.imb_done_crop);
        imb_RotateLeft = (ImageButton) findViewById(R.id.imb_rotate_left);
        imb_RotateRight = (ImageButton) findViewById(R.id.imb_rotate_right);
        imb_FlipHorizontal = (ImageButton) findViewById(R.id.imb_flip_horizontal);
        imb_FlipVertical = (ImageButton) findViewById(R.id.imb_flip_vertical);

        rbt_RatioFree = (RadioButton) findViewById(R.id.rbt_free);
        rbt_RatioSquare = (RadioButton) findViewById(R.id.rbt_11);
        rbt_Ratio34 = (RadioButton) findViewById(R.id.rbt_34);
        rbt_Ratio43 = (RadioButton) findViewById(R.id.rbt_43);
        rbt_Ratio916 = (RadioButton) findViewById(R.id.rbt_916);
        rbt_Ratio169 = (RadioButton) findViewById(R.id.rbt_169);

        rbt_RatioFree.setChecked(true);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCropImageUri = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE);
        mOptions = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS);
        mCropImageView.setImageUriAsync(mCropImageUri);
        mCropImageView.setOnCropImageCompleteListener(this);

        imb_Home.setOnClickListener(this);
        imb_Done.setOnClickListener(this);
        imb_RotateLeft.setOnClickListener(this);
        imb_RotateRight.setOnClickListener(this);
        imb_FlipVertical.setOnClickListener(this);
        imb_FlipHorizontal.setOnClickListener(this);

        rbt_RatioFree.setOnClickListener(this);
        rbt_RatioSquare.setOnClickListener(this);
        rbt_Ratio34.setOnClickListener(this);
        rbt_Ratio43.setOnClickListener(this);
        rbt_Ratio916.setOnClickListener(this);
        rbt_Ratio169.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_home_crop:
                onBackPressed();
                break;
            case R.id.imb_done_crop:
                cropImage();
                break;
            case R.id.imb_rotate_left:
                rotateImage(-90);
                break;
            case R.id.imb_rotate_right:
                rotateImage(90);
                break;
            case R.id.imb_flip_horizontal:
                mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
                break;
            case R.id.imb_flip_vertical:
                mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.HORIZONTAL));
                break;
            case R.id.rbt_free:
                if (mCropImageView.isFixAspectRatio()) {
                    mCropImageView.clearAspectRatio();
                }
                break;
            case R.id.rbt_11:
                mCropImageView.setAspectRatio(1, 1);
                break;
            case R.id.rbt_34:
                mCropImageView.setAspectRatio(3, 4);
                break;
            case R.id.rbt_43:
                mCropImageView.setAspectRatio(4, 3);
                break;
            case R.id.rbt_916:
                mCropImageView.setAspectRatio(9, 16);
                break;
            case R.id.rbt_169:
                mCropImageView.setAspectRatio(16, 9);
                break;
        }

    }


    private Bitmap flip(Bitmap src, Direction type) {
        Matrix matrix = new Matrix();
        if (type == Direction.HORIZONTAL) {
            matrix.preScale(1.0f, -1.0f);
        } else if (type == Direction.VERTICAL) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return src;
        }
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private enum Direction {
        VERTICAL, HORIZONTAL
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            if (mOptions.initialCropWindowRectangle != null) {
                mCropImageView.setCropRect(mOptions.initialCropWindowRectangle);
            }
            if (mOptions.initialRotation > -1) {
                mCropImageView.setRotatedDegrees(mOptions.initialRotation);
            }
        } else {
            setResult(null, error, 1);
        }

        mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
        mCropImageView.setImageBitmap(flip(mCropImageView.getImageBitmap(), Direction.VERTICAL));
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        startEditActivity(result.getUri());
    }

    private void startEditActivity(Uri uri) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.setData(uri);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    //region: Private methods

    /**
     * Execute crop image and save the result tou output uri.
     */
    protected void cropImage() {
        if (mOptions.noOutputImage) {
            setResult(null, null, 1);
        } else {
            Uri outputUri = getOutputUri();
            mCropImageView.saveCroppedImageAsync(outputUri,
                    mOptions.outputCompressFormat,
                    mOptions.outputCompressQuality,
                    mOptions.outputRequestWidth,
                    mOptions.outputRequestHeight,
                    mOptions.outputRequestSizeOptions);
        }
    }

    /**
     * Rotate the image in the crop image view.
     */
    protected void rotateImage(int degrees) {
        mCropImageView.rotateImage(degrees);
    }

    /**
     * Get Android uri to save the cropped image into.<br>
     * Use the given in options or create a temp file.
     */
    protected Uri getOutputUri() {
        Uri outputUri = mOptions.outputUri;
        if (outputUri.equals(Uri.EMPTY)) {
            try {
                String ext = mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" :
                        mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
                outputUri = Uri.fromFile(File.createTempFile("cropped", ext, getCacheDir()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp file for output image", e);
            }
        }
        return outputUri;
    }

    /**
     * Result with cropped image data or error if failed.
     */
    protected void setResult(Uri uri, Exception error, int sampleSize) {
        int resultCode = error == null ? RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
        setResult(resultCode, getResultIntent(uri, error, sampleSize));
        finish();
    }

    /**
     * Cancel of cropping activity.
     */
    protected void setResultCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Get intent instance to be used for the result of this activity.
     */
    protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
        CropImage.ActivityResult result = new CropImage.ActivityResult(null,
                uri,
                error,
                mCropImageView.getCropPoints(),
                mCropImageView.getCropRect(),
                mCropImageView.getRotatedDegrees(),
                sampleSize);
        Intent intent = new Intent();
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
        return intent;
    }
    //endregion
}
