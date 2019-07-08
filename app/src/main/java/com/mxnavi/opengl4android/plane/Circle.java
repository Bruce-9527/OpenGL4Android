package com.mxnavi.opengl4android.plane;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * @author xuebb
 * @description 圆形
 * @date: 2019/7/4 14:24
 */
public class Circle extends Shape {

    //顶点之间的偏移量
    private final int vertexStride = 0; // 每个顶点四个字节

    private int mMatrixHandler;

    private float radius = 0.5f;
    private int n = 360;  //切割份数

    private float[] shapePos;
    private float height = 0.0f;

    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {0.3f, 0.6f, 0.9f, 1.0f};

    public Circle() {
    }


    public Circle(float height) {
        this.height = height;
    }


    public void setRadius(float radius) {
        this.radius = radius;
    }

    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        data.add(height);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    @Override
    public void init() {
        shapePos = createPositions();
        ByteBuffer bb = ByteBuffer.allocateDirect(
                shapePos.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapePos);
        vertexBuffer.position(0);
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onDraw(float[] mMVPMatrix) {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, shapePos.length / 3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

}
