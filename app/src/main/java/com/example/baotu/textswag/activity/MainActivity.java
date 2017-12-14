package com.example.baotu.textswag.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.baotu.textswag.R;
import com.example.baotu.textswag.cropper.CropImage;
import com.example.baotu.textswag.cropper.CropImageView;
import com.example.baotu.textswag.other.Defines;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private ImageButton imb_Camera;
    private ImageButton imb_Album;
    private ImageButton imb_MoreApp;
    private ImageButton imb_Gallery;
    private RelativeLayout rl_Ads;

    private Uri uriPhotoFromCamera;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {
        imb_Album = (ImageButton) findViewById(R.id.imb_album);
        imb_Camera = (ImageButton) findViewById(R.id.imb_camera);
        imb_Gallery = (ImageButton) findViewById(R.id.imb_gallery);
        imb_MoreApp = (ImageButton) findViewById(R.id.imb_more_app);
        rl_Ads = (RelativeLayout) findViewById(R.id.rl_ads);
        imb_Album.setOnClickListener(this);
        imb_MoreApp.setOnClickListener(this);
        imb_Gallery.setOnClickListener(this);
        imb_Camera.setOnClickListener(this);
        imb_Album.setVisibility(View.INVISIBLE);
        imb_Camera.setVisibility(View.INVISIBLE);
        imb_Gallery.setVisibility(View.INVISIBLE);
        imb_MoreApp.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(permissionsRequired, Defines.PERMISSION_CALLBACK_CONSTANT);

            }

        } else {
            proceedAfterPermission();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Defines.REQUEST_PICK_IMAGE) {
            startCropActivity(data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Defines.REQUEST_CAPTURE_CAMERA) {
            startCropActivity(uriPhotoFromCamera);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Defines.PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted.
            boolean granted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                } else {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                proceedAfterPermission();
            } else {
                Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void proceedAfterPermission() {
        final Animation inLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        final Animation inRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imb_Album.setVisibility(View.VISIBLE);
                imb_MoreApp.setVisibility(View.VISIBLE);
                imb_Gallery.setVisibility(View.VISIBLE);
                imb_Camera.setVisibility(View.VISIBLE);
                imb_Album.startAnimation(inLeft);
                imb_MoreApp.startAnimation(inLeft);
                imb_Camera.startAnimation(inRight);
                imb_Gallery.startAnimation(inRight);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_album:
                chooseImageFromAlbum();
                break;
            case R.id.imb_camera:
                captureImageFromCamera();
                break;
            case R.id.imb_gallery:
                startGalleryActivity();
                break;
            case R.id.imb_more_app:
                startMoreAppActivity();
                break;
            default:
                break;
        }
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    private void startMoreAppActivity() {
        Intent intent = new Intent(this, MoreAppActivity.class);
        startActivity(intent);
    }

    private void chooseImageFromAlbum() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, Defines.REQUEST_PICK_IMAGE);
    }

    private void captureImageFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uriImage;

        File phoFile = null;
        try {
            phoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (phoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String s = BuildConfig.APPLICATION_ID + ".provider";
                uriImage = FileProvider.getUriForFile(getApplicationContext(), s, phoFile);
            } else {
                uriImage = Uri.fromFile(phoFile);
            }
            uriPhotoFromCamera = uriImage;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
            startActivityForResult(intent, Defines.REQUEST_CAPTURE_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Defines.PHOTO_FILE_NAME + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void startCropActivity(Uri uri) {
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setMultiTouchEnabled(true)
                .start(this);
    }

}
