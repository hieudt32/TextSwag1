package com.example.baotu.textswag.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baotu.textswag.R;

public class InputActivity extends BaseActivity implements View.OnClickListener {
    private EditText edt_Input;
    private Button btn_Ok, btn_Cancel;

    @Override
    protected int getLayoutResource() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setFinishOnTouchOutside(false);
        return R.layout.activity_dialog_input;
    }

    @Override
    protected void initVariables() {
        edt_Input = (EditText) findViewById(R.id.edt_input);
        btn_Ok = (Button) findViewById(R.id.btn_ok_input);
        btn_Cancel = (Button) findViewById(R.id.btn_cancel_input);
    }

    @Override
    protected void initData() {
        btn_Cancel.setOnClickListener(this);
        btn_Ok.setOnClickListener(this);
    }

    private void onOKPress(String text) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(text));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_input:
                finish();
                break;
            case R.id.btn_ok_input:
                String text = edt_Input.getText().toString().trim();
                if (text.compareTo("") != 0) {
                    onOKPress(text);
                } else {
                    Toast.makeText(this, "You need to enter text", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edt_input:
                break;
        }
    }
}
