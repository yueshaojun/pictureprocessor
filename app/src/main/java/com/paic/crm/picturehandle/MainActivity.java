package com.paic.crm.picturehandle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import static android.view.Window.FEATURE_NO_TITLE;

/**
 * @author yueshaojun988
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button saveBtn;
    MatrixView matrixView;
    Button addPicBtn;
    Button clipBtn;
    Button signatureBtn;
    FrameLayout container;
    EditText editText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);
        saveBtn = (Button) findViewById(R.id.btn_photo);
        saveBtn.setOnClickListener(this);
        matrixView = (MatrixView) findViewById(R.id.matrix_view);
        matrixView.setText("岳绍君");
        matrixView.setFontSize(50);
        matrixView.setBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round));
        matrixView.setTextColor(Color.parseColor("#000000"));
        addPicBtn = (Button) findViewById(R.id.btn_text);
        addPicBtn.setOnClickListener(this);
        container.setOnClickListener(this);
        editText = new EditText(this);
        clipBtn = (Button) findViewById(R.id.btn_clip);
        clipBtn.setOnClickListener(this);

        signatureBtn = (Button) findViewById(R.id.btn_signature);
        signatureBtn.setOnClickListener(this);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_photo) {

            PictureHandleTool.writeToFile(container);
        }

        if(v.getId() == R.id.btn_text){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                editText.setBackground(new ColorDrawable(Color.parseColor("#FFB6C1")));
            }
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(matrixView.getWidth(),matrixView.getHeight());
            container.addView(editText,flp);
            editText.setTranslationX(matrixView.getFingerPointX());
            editText.setTranslationY(matrixView.getFingerPointY());
            editText.setFocusable(true);
            editText.requestFocus();
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        matrixView.setText(((EditText)v).getText().toString());
                    }
                }
            });
        }
        if(v.getId() == R.id.container){
            container.requestFocus();
            container.removeView(editText);

        }
        if(v.getId() == R.id.btn_clip){

            PictureHandleTool.createClipImg(container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"click",Toast.LENGTH_LONG).show();
                }
            });

        }

        if(v.getId() == R.id.btn_signature){
            startActivityForResult(new Intent(this,Main2Activity.class),1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK){
            return;
        }
        matrixView.setBitmap((Bitmap) data.getExtras().get("signatureView"));
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}
