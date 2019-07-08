package com.mxnavi.opengl4android.base;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;

/**
 * 存储系统矩阵状态的类
 */
public class MatrixState {
    //4x4矩阵 投影用
    private float[] mProjMatrix = new float[16];
    //摄像机位置朝向9参数矩阵
    private float[] mVMatrix = new float[16];
    //当前变换矩阵
    private float[] currMatrix;
    //定位光光源位置
    public float[] lightLocation = new float[]{0, 0, 0};
    public FloatBuffer cameraFB;
    public FloatBuffer lightPositionFB;
    //保护变换矩阵的栈
    public Stack<float[]> mStack = new Stack<float[]>();

    public void setInitStack()//获取不变换初始矩阵
    {
        currMatrix = new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    public void pushMatrix()//保护变换矩阵
    {
        mStack.push(currMatrix.clone());
    }

    public void popMatrix()//恢复变换矩阵
    {
        currMatrix = mStack.pop();
    }

    public void translate(float x, float y, float z)//设置沿xyz轴移动
    {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z)//设置绕xyz轴移动
    {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }


    /**
     * 设置摄像机
     * @param cx
     * @param cy
     * @param cz
     * @param tx
     * @param ty
     * @param tz
     * @param upx
     * @param upy
     * @param upz
     */
    public void setCamera
    (
            float cx,    //摄像机位置x
            float cy,   //摄像机位置y
            float cz,   //摄像机位置z
            float tx,   //摄像机目标点x
            float ty,   //摄像机目标点y
            float tz,   //摄像机目标点z
            float upx,  //摄像机UP向量X分量
            float upy,  //摄像机UP向量Y分量
            float upz   //摄像机UP向量Z分量
    ) {
        Matrix.setLookAtM
                (
                        mVMatrix,
                        0,
                        cx,
                        cy,
                        cz,
                        tx,
                        ty,
                        tz,
                        upx,
                        upy,
                        upz
                );

        float[] cameraLocation = new float[3];//摄像机位置
        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;

        ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        cameraFB = llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }

    /**
     * 设置透视投影参数
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public void setProjectFrustum
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 设置正交投影参数
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public void setProjectOrtho
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 获取具体物体的总变换矩阵
     * @return
     */
    public float[] getFinalMatrix() {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    /**
     * 获取具体物体的变换矩阵
     * @return
     */
    public float[] getMMatrix() {
        return currMatrix;
    }

    /**
     * 设置灯光位置的方法
     * @param x
     * @param y
     * @param z
     */
    public void setLightLocation(float x, float y, float z) {
        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;
        ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        lightPositionFB = llbb.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }
}
