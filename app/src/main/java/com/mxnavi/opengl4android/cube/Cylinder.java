package com.mxnavi.opengl4android.cube;

import android.opengl.GLES20;
import android.view.View;

import com.mxnavi.opengl4android.base.ShaderUtil;
import com.mxnavi.opengl4android.plane.Circle;
import com.mxnavi.opengl4android.plane.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * @author xuebb
 * @description 圆柱体
 * @date: 2019/7/4 15:27
 */
public class Cylinder extends Shape {

    private View view;

    private Circle ovalBottom, ovalTop;
    private FloatBuffer vertexBuffer;

    //切割份数
    private int n = 360;
    //圆柱高度
    private float height = 1.0f;
    //圆柱底面半径
    private float radius = 0.5f;

    private int vSize;

    public Cylinder(View view) {
        this.view = view;
    }

    @Override
    public void init() {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // 初始化
        ovalBottom = new Circle();
        ovalTop = new Circle(height);
        ArrayList<Float> pos = new ArrayList<>();
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(height);
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(0.0f);
        }
        float[] d = new float[pos.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = pos.get(i);
        }
        vSize = d.length / 3;
        ByteBuffer buffer = ByteBuffer.allocateDirect(d.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(d);
        vertexBuffer.position(0);
        //加载顶点着色器的脚本内容
        String mVertexShader = ShaderUtil.loadFromAssetsFile("shader/cone_vertex.sh", view.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader = ShaderUtil.loadFromAssetsFile("shader/cone_frag.sh", view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        ovalBottom.init();
        ovalTop.init();
    }

    @Override
    public void onDraw(float[] mMVPMatrix) {
        GLES20.glUseProgram(mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        ovalBottom.onDraw(mMVPMatrix);
        ovalTop.onDraw(mMVPMatrix);
    }
}
