package com.mxnavi.opengl4android.plane;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.mxnavi.opengl4android.ShapeRenderer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author xuebb
 * @description 平面渲染renderer
 * @date: 2019/6/11 13:59
 */
public class PlaneGlRenderer extends ShapeRenderer {


    private Shape shape;

    public void setShape(Shape shape) {
        this.shape = shape;
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);
        // 初始化图形
        if (shape == null) {
            shape = new Triangle();
        }
        shape.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        super.onSurfaceChanged(gl10, width, height);
        float ratio = (float) width / height;
        // 这个投影矩阵被应用于对象坐标在onDrawFrame（）方法中
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

    }

    private float[] mRotationMatrix = new float[16];

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // 创建一个旋转矩阵
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        // Set the camera position (View matrix)
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//        // Draw shape
        shape.onDraw(mMVPMatrix);

    }


}
