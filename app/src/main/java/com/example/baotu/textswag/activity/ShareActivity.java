package com.example.baotu.textswag.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.baotu.textswag.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

public class ShareActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private ImageButton imb_Home;
    private FloatingActionMenu fam_Menu;
    private FloatingActionButton fab_Facebook, fab_Twitter, fab_Instagram;
    private ImageSwitcher ims_Preview;
    private Uri uri;
    private ArrayList<String> mListImage = null;
    private float lastX;
    private int mPosition;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_share;
    }

    @Override
    protected void initVariables() {
        imb_Home = (ImageButton) findViewById(R.id.imb_home_share);
        ims_Preview = (ImageSwitcher) findViewById(R.id.ims_preview_share);

        fam_Menu = (FloatingActionMenu) findViewById(R.id.fam_menu);
        fab_Facebook = (FloatingActionButton) findViewById(R.id.fab_facebook);
        fab_Instagram = (FloatingActionButton) findViewById(R.id.fab_instagram);
        fab_Twitter = (FloatingActionButton) findViewById(R.id.fab_twitter);
    }

    @Override
    protected void initData() {
        imb_Home.setOnClickListener(this);
        fab_Facebook.setOnClickListener(this);
        fab_Twitter.setOnClickListener(this);
        fab_Instagram.setOnClickListener(this);

        String uriImage = getIntent().getExtras().getString("URI_IMAGE");
        mListImage = getIntent().getExtras().getStringArrayList("LIST_IMAGE");
        mPosition = getIntent().getExtras().getInt("POSITION");
        uri = Uri.parse(uriImage);
        ims_Preview.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0x00000000);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams( ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        ims_Preview.setInAnimation(in);
        ims_Preview.setOutAnimation(out);

        ims_Preview.setImageURI(uri);
        ims_Preview.setOnTouchListener(this);
    }

    /**
     * Facebook - "com.facebook.katana"
     * Twitter - "com.twitter.android"
     * LinkedIn - "com.linkedin.android"
     * Instagram - "com.instagram.android"
     * Pinterest - "com.pinterest"
     *
     * @param packageName
     * @param bmpUri
     */

    private void sharingToSocialMedia(String packageName, Uri bmpUri) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);

        boolean installed = checkAppInstall(packageName);
        if (installed) {
            intent.setPackage(packageName);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Installed application first", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_home_share:
                backToHome();
                break;
            case R.id.fab_facebook:
                fam_Menu.close(true);
                sharingToSocialMedia("com.facebook.katana", uri);
                break;
            case R.id.fab_instagram:
                fam_Menu.close(true);
                sharingToSocialMedia("com.instagram.android", uri);
                break;
            case R.id.fab_twitter:
                fam_Menu.close(true);
                sharingToSocialMedia("com.twitter.android", uri);
                break;
        }
    }

    private void backToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                if (lastX < currentX) {
                    if (mListImage != null) {
                        if (mPosition > 0) {
                            mPosition--;
                            uri = Uri.parse("file://" + mListImage.get(mPosition));
                            ims_Preview.setImageURI(uri);
                        }
                    }
                }
                if (lastX > currentX) {
                    if (mListImage != null) {
                        if (mPosition < mListImage.size() - 1) {
                            mPosition++;
                            uri = Uri.parse("file://" + mListImage.get(mPosition));
                            ims_Preview.setImageURI(uri);
                        }
                    }
                }

                break;
            default:
                return false;
        }
        return true;
    }
}
