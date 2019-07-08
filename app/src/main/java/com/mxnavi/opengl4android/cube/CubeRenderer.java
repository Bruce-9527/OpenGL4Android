package com.mxnavi.opengl4android.cube;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.mxnavi.opengl4android.ShapeRenderer;
import com.mxnavi.opengl4android.plane.Shape;
import com.mxnavi.opengl4android.plane.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author xuebb
 * @description 立方体渲染renderer
 * @date: 2019/6/12 16:16
 */
public class CubeRenderer extends ShapeRenderer {

    /**
     * 立方体
     */
    private Shape shape;

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        if (shape == null) {
            shape = new Cube();
        }
        // 初始化立方体
        shape.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        if (shape instanceof Cube) {
            //为了能清除的看清正方体的六个面，改变下相机位置
            Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        } else {
            Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }
//
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//        Matrix.rotateM(mMVPMatrix, 0, 95, 1, 0, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        shape.onDraw(mMVPMatrix);
    }
}
