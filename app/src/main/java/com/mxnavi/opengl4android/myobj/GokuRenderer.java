package com.mxnavi.opengl4android.myobj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;


import com.mxnavi.opengl4android.base.LeGLConfig;
import com.mxnavi.opengl4android.base.MatrixState;
import com.mxnavi.opengl4android.base.ObjInfo;
import com.mxnavi.opengl4android.plane.PlaneGlSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 场景的渲染器
 *
 * @author xuebb
 */
public class GokuRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GokuRenderer";
    /**
     * UI
     */
    GokuGroup mSpriteGroup = null;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    MatrixState matrixState;

    public PlaneGlSurfaceView mGLSurfaceView;

    public GokuRenderer(PlaneGlSurfaceView glSurfaceView) {
        this.mGLSurfaceView = glSurfaceView;
        matrixState = new MatrixState();
        // 初始化obj+mtl文件
        mSpriteGroup = new GokuGroup(mGLSurfaceView);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO GlThread
        // 清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // 设置屏幕背景色RGBA
        /**
         * 绘制物体
         */
        matrixState.pushMatrix();
        mSpriteGroup.onDraw(matrixState);
        matrixState.popMatrix();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO GlThread
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //开启混合
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        // 设置屏幕背景色RGBA
        //GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // 启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // 设置为打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // 初始化变换矩阵
        matrixState.setInitStack();
        matrixState.setLightLocation(1000, 1000, 1000);
        //
        initUI();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO GlThread
        // viewPort
        GLES20.glViewport(0, 0, width, height);
        //

        float ratio = (float) width / height;
        // 平行投影
//		MatrixState.setProjectOrtho(-ratio, ratio, -1, 1,
//				LeGLConfig.PROJECTION_NEAR, LeGLConfig.PROJECTION_FAR);
        matrixState.setProjectFrustum(-ratio, ratio, -1, 1,
                LeGLConfig.PROJECTION_NEAR, LeGLConfig.PROJECTION_FAR);
        // camera
        matrixState.setCamera(LeGLConfig.EYE_X, LeGLConfig.EYE_Y, LeGLConfig.EYE_Z,
                LeGLConfig.VIEW_CENTER_X, LeGLConfig.VIEW_CENTER_Y, LeGLConfig.VIEW_CENTER_Z,
                0f, 1f, 0f);
    }

    /**
     * 初始化场景中的精灵实体类
     */
    private void initUI() {
        mSpriteGroup.initObjs();
    }

    public PlaneGlSurfaceView.OnTouchEventListener getTouchEventListener() {
        return touchEventListener;
    }

    /**
     * 触摸回调
     */
    PlaneGlSurfaceView.OnTouchEventListener touchEventListener = new PlaneGlSurfaceView.OnTouchEventListener() {
        @Override
        public void onTouchEvent(float dx, float dy) {
            float yAngle = mSpriteGroup.getSpriteAngleY();
            yAngle += dx * TOUCH_SCALE_FACTOR;
            mSpriteGroup.setSpriteAngleY(yAngle);

//            float xAngle = mSpriteGroup.getSpriteAngleX();
//            xAngle += dy * TOUCH_SCALE_FACTOR;
//            mSpriteGroup.setSpriteAngleX(xAngle);

            mGLSurfaceView.requestRender();//重绘画面
        }
    };


}
