package com.mxnavi.opengl4android;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author xuebb
 * @description Gl渲染基础类
 * @date: 2019/6/13 9:18
 */
public abstract class ShapeRenderer implements GLSurfaceView.Renderer{

    /**
     * 相乘矩阵
     */
    protected final float[] mMVPMatrix = new float[16];
    /**
     * 透视投影矩阵
     */
    protected final float[] mProjectionMatrix = new float[16];
    /**
     * 相机视图
     */
    protected final float[] mViewMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 清屏 红，绿，蓝，透明度
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //在每次Surface尺寸变化时回调，例如当设备的屏幕方向发生改变时
        //设置视图的尺寸，这就告诉了OpenGL可以用来渲染surface的大小
        GLES20.glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    public static int loadShader(int type, String shaderCode) {
        // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
        // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // 添加上面编写的着色器代码并编译它
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
