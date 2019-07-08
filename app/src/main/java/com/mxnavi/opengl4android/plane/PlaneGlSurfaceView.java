package com.mxnavi.opengl4android.plane;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author xuebb
 * @description 平面GLSurfaceView
 * @date: 2019/6/11 13:58
 */
public class PlaneGlSurfaceView extends GLSurfaceView {

    private OnTouchEventListener touchListener;

    public PlaneGlSurfaceView(Context context) {
        super(context);
        init();
    }

    public PlaneGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
    }

    public void setOnTouchListener(OnTouchEventListener listener) {
        touchListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        this.setSceneWidthAndHeight(this.getMeasuredWidth(),
                this.getMeasuredHeight());
    }

    public void setSceneWidthAndHeight(float mSceneWidth, float mSceneHeight) {
        this.mSceneWidth = mSceneWidth;
        this.mSceneHeight = mSceneHeight;
    }

    // 宽
    private float mSceneWidth = 720;
    // 高
    private float mSceneHeight = 1280;

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标


    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //计算触控笔Y位移
                float dy = y - mPreviousY;
                //计算触控笔X位移
                float dx = x - mPreviousX;
                //
                if (touchListener != null) {
                    touchListener.onTouchEvent(dx, dy);
                }
                break;
            default:
                break;

        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

    /**
     * 触摸监听接口
     */
    public interface OnTouchEventListener {

        void onTouchEvent(float dx, float dy);

    }


}
