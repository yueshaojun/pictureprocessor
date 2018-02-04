package com.paic.crm.picturehandle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    Button clearBtn;
    Button useBtn;
    SignatureView signatureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        clearBtn = (Button) findViewById(R.id.btn_clear);
        clearBtn.setOnClickListener(this);

        useBtn = (Button) findViewById(R.id.btn_use);
        useBtn.setOnClickListener(this);

        signatureView = (SignatureView) findViewById(R.id.vw_signature);
        signatureView.setStrokeWidth(20f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear :
                signatureView.clear();
                break;
            case R.id.btn_use:
                Intent dataIntent = new Intent();
                Bundle dataBundle = new Bundle();
                dataBundle.putParcelable("signatureView",signatureView.getBitmap(200,200, Bitmap.Config.ARGB_8888));
                dataIntent.putExtras(dataBundle);
                setResult(RESULT_OK,dataIntent);
                finish();
            default:
        }
    }
}
