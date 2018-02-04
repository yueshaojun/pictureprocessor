package com.paic.crm.picturehandle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author yueshaojun988
 * @date 2018/1/4
 */

public class MatrixView extends View {
    private Bitmap mBitmap;
    private Camera camera ;
    private Matrix matrix;
    private Paint paint;

    private float downPointX;

    private float downPointY;

    private Context mContext;

    private Point fingerPoint;
    private String mText;

    private int mFontSize;
    private int mTextColor;

    private static final int DEFAULT_SIZE = 200;
    private static final float DEFAULT_STROKE_WIDTH = 5f;
    public MatrixView(Context context) {
        super(context);
        init(context);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        camera = new Camera();
        matrix = new Matrix();
        setClickable(true);
        setLongClickable(true);
        fingerPoint = new Point();
        fingerPoint.x = 0;
        fingerPoint.y = 0;
        mContext = context;
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(mTextColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        paint.setTextSize(mFontSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int realHeight = MeasureSpec.getSize(heightMeasureSpec);
        Drawable background = getBackground();
        if(widthMode == MeasureSpec.AT_MOST){
            if(background != null) {
                realWidth = background.getIntrinsicWidth();
            }else {
                realWidth = DEFAULT_SIZE;
            }
        }
        if(heightMode == MeasureSpec.AT_MOST){
            if(background != null) {
                realHeight = getBackground().getIntrinsicHeight();
            }else {
                realHeight = DEFAULT_SIZE;
            }
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(realWidth,widthMode),
                MeasureSpec.makeMeasureSpec(realHeight,heightMode));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, matrix, paint);
        }

        canvas.drawText(mText,getTextDrawStart(),getTextBaseline(),paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("MatrixView","onTouchEvent");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(downPointX == 0) {
                    downPointX = event.getRawX();
                }
                if(downPointY == 0) {
                    downPointY = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaX = (event.getRawX() - downPointX);
                //x方向上 不能超出父类view
                if(deltaX <0){
                    deltaX = 0 ;
                }

                setTranslationX(Math.min(deltaX,((ViewGroup)getParent()).getWidth()-getWidth()));

                float deltaY = (event.getRawY() - downPointY);
                //y方向上 不能超出父类view
                if(deltaY <0){
                    deltaY = 0;
                }
                setTranslationY(Math.min(deltaY,((ViewGroup)getParent()).getHeight()-getHeight()));

                fingerPoint.x = (int) (getLeft()+deltaX);
                fingerPoint.y = (int) (getTop()+deltaY);
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:

                downPointX = event.getRawX();
                downPointY = event.getRawY();

                break;
            default:
                Log.i("MatrixView","defaultAction"+event.getAction());
        }
        return super.onTouchEvent(event);
    }
    /** 设置文字内容
     * @param text 文字内容
     * @return
     */
    public void setText(String text){
        mText = text;
        invalidate();
    }

    /**
     * 设置文字大小
     * @param fontSize 大小
     */
    public void setFontSize(int fontSize){
        mFontSize = fontSize;
        paint.setTextSize(fontSize);
        invalidate();
    }

    /**
     * 设置文字颜色
     * @param color 颜色
     */
    public void setTextColor(int color){
        mTextColor = color;
        paint.setColor(color);
        invalidate();
    }

    public int getFingerPointX() {
        return fingerPoint.x;
    }
    public int getFingerPointY() {
        return fingerPoint.y;
    }

    /**
     * 文字水平移动到中间
     * @return
     */
    private float getTextDrawStart(){
        int textLength = mText.length();
        return getWidth()/2-textLength*mFontSize/2;
    }
    /**
     * 文字竖直移动到中间
     * @return
     */
    private float getTextBaseline(){
        return getHeight()/2+mFontSize/2;
    }
    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        invalidate();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void clear(){
        mBitmap.recycle();
        paint.reset();
        invalidate();
    }
}
