package com.mxnavi.opengl4android.cube;

import android.opengl.GLES20;
import android.view.View;

import com.mxnavi.opengl4android.base.ShaderUtil;
import com.mxnavi.opengl4android.plane.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * @author xuebb
 * @description 球体
 * @date: 2019/7/4 16:57
 */
public class Ball extends Shape {

    private View view;

    private float step = 5f;
    private int vSize;

    public Ball(View view) {
        this.view = view;
    }


    /**
     * 根据经纬度法计算球面坐标（以下公式为球面坐标系公式，与openGl系有出入）
     * x=Rsinθcosφ
     * y=Rsinθsinφ
     * z=Rcosθ
     * 注明：原点到 P 点的距离 r ，原点到点 P 的连线与正 z轴之间的天顶角θ  以及原点到点 P 的连线，在 xy-平面的投影线，与正 x轴之间的方位角φ
     *
     * @return
     */
    private float[] createBallPos() {
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;
        // 遍历纬度
        for (float i = -90; i < 90 + step; i += step) {
            //Math.sin(x)  x 的正玄值。返回值在 -1.0 到 1.0 之间；X 都是指的“弧度”而非“角度”，弧度的计算公式为： 2*PI/360*角度
            //计算当前点与圆心连线，与Y轴夹角θ的正余弦值（此坐标系为openGL坐标系）
            /**
             * 以下坐标系为openGl坐标系
             * 此处计算当前纬度与Z轴夹角的正余弦值，由于根据公式，应该计算与Y轴的正余弦，但因为互余夹角sin与cos相等，所以按此计算没有问题
             * 后续的X的坐标就变成了x=R*cos*cos,所有关于θ的角的sin值都变换为cos计算
             */
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float) Math.sin(i * Math.PI / 180.0);
            h2 = (float) Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                // 计算当前点在XZ平面投影，与Z轴的夹角φ的正余弦值（此坐标系为openGL坐标系）
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = (float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    @Override
    public void init() {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        float[] dataPos = createBallPos();
        ByteBuffer buffer = ByteBuffer.allocateDirect(dataPos.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(dataPos);
        vertexBuffer.position(0);
        vSize = dataPos.length / 3;

        //加载顶点着色器的脚本内容
        String mVertexShader = ShaderUtil.loadFromAssetsFile("shader/ball_vertex.sh", view.getResources());
        //加载片元着色器的脚本内容
        String mFragmentShader = ShaderUtil.loadFromAssetsFile("shader/cone_frag.sh", view.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
    }

    @Override
    public void onDraw(float[] mMVPMatrix) {
        GLES20.glUseProgram(mProgram);
        int mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
