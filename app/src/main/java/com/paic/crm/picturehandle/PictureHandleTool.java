package com.paic.crm.picturehandle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author yueshaojun988
 * @date 2018/1/9
 */

public class PictureHandleTool {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r,"PictureHandle");
        }
    });
    public static Bitmap drawContainerBitmap(ViewGroup container) {
        Bitmap bitmap = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_8888);
        container.draw(new Canvas(bitmap));
        return bitmap;
    }
    public static void writeToFile(ViewGroup container){
        String imagePath = container.getContext().getExternalFilesDir("screenshot").getAbsoluteFile()
                +
                "/"
                +
                SystemClock.currentThreadTimeMillis() + ".png";
        writeToFile(container,imagePath);

    }
    public static void writeToFile(ViewGroup container,String filePath){
        writeToFile(drawContainerBitmap(container),filePath);
    }
    public static void writeToFile(Bitmap bitmap){
        writeToFile(bitmap);
    }
    public static void writeToFile(final Bitmap bitmap , final String filePath){

        executor.submit(new Runnable() {
            @Override
            public void run() {
                File picFile = new File(filePath);
                if(!picFile.exists()){
                    try {
                        picFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(picFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(fileOutputStream != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        bitmap.recycle();
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    public static ImageView createClipImg(ViewGroup container){
       return createClipImg(container,container.getWidth()/4*3,container.getHeight()/4*3);
    }
    public static ImageView createClipImg(ViewGroup container,int width,int height){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,height);
        lp.gravity = Gravity.CENTER;
        final ImageView imageView = new ImageView(container.getContext());
        container.addView(imageView,lp);
        Rect rect = new Rect();
        container.getDrawingRect(rect);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        BitmapDrawable bitmapDrawable = new BitmapDrawable(container.getContext().getResources(),drawContainerBitmap(container));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{colorDrawable,bitmapDrawable});
        layerDrawable.setLayerInset(0,0,0,0,0);
        layerDrawable.setLayerInset(1,20,20,20,20);
        imageView.setImageDrawable(layerDrawable);

        return imageView;
    }
}
