package com.mxnavi.opengl4android.myobj;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;


import com.mxnavi.opengl4android.base.GLEntity;
import com.mxnavi.opengl4android.base.MatrixState;
import com.mxnavi.opengl4android.base.ShaderUtil;
import com.mxnavi.opengl4android.base.TextureUtil;
import com.mxnavi.opengl4android.plane.PlaneGlSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 加载后的物体
 */
public class GokuEntity extends GLEntity {
    //自定义渲染管线着色器程序id
    int mProgram;
    //总变换矩阵引用
    int muMVPMatrixHandle;
    //位置、旋转变换矩阵
    int muMMatrixHandle;
    //顶点位置属性引用
    int maPositionHandle;
    //顶点法向量属性引用
    int maNormalHandle;
    //光源位置属性引用
    int maLightLocationHandle;
    //摄像机位置属性引用
    int maCameraHandle;
    //顶点纹理坐标属性引用
    int maTexCoorHandle;
    // 顶点颜色
    int muColorHandle;
    // 材质中透明度
    int muOpacityHandle;
    //顶点着色器代码脚本
    String mVertexShader;
    //片元着色器代码脚本
    String mFragmentShader;

    //顶点坐标数据缓冲
    FloatBuffer mVertexBuffer;
    //顶点法向量数据缓冲
    FloatBuffer mNormalBuffer;
    //顶点纹理坐标数据缓冲
    FloatBuffer mTexCoorBuffer;

    // 材质中alpha
    protected float mAlpha;
    // 需转化为纹理的图片
    protected Bitmap mBmp;
    //
    int vCount = 0;
    /**
     *
     */
    // 纹理是否已加载
    protected boolean isInintFinsh = false;
    // 纹理id
    protected int textureId;


    public GokuEntity(PlaneGlSurfaceView scene, float[] vertices, float[] normals, float texCoors[], float alpha, Bitmap bmp) {
        //初始化顶点坐标与着色数据
        initVertexData(vertices, normals, texCoors, alpha, bmp);
        //初始化shader
        initShader(scene.getResources());
    }

    //初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices, float[] normals, float texCoors[], float alpha, Bitmap bmp) {
        this.mAlpha = alpha;
        this.mBmp = bmp;
        //顶点坐标数据的初始化================begin============================
        vCount = vertices.length / 3;

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点法向量数据的初始化================begin============================
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mNormalBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mNormalBuffer.put(normals);//向缓冲区中放入顶点法向量数据
        mNormalBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点着色数据的初始化================end============================

        //顶点纹理坐标数据的初始化================begin============================
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoors);//向缓冲区中放入顶点纹理坐标数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    //初始化shader
    public void initShader(Resources res) {
        //加载顶点着色器的脚本内容
        mVertexShader = ShaderUtil.loadFromAssetsFile("shader/texture_vertex.sh", res);
        //加载片元着色器的脚本内容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("shader/texture_frag.sh", res);
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点颜色属性引用
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        //获取程序中光源位置引用
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中摄像机位置引用
        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        // 顶点颜色
        muColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        // alpha
        muOpacityHandle = GLES20.glGetUniformLocation(mProgram, "uOpacity");
    }

    /**
     * 初始化纹理
     */
    private void initTexture() {
        // 两球之间连线的纹理图片
        if (mBmp != null) {
            textureId = TextureUtil.getTextureIdByBitmap(mBmp);
        }
    }


    @Override
    public void onDraw(MatrixState matrixState) {
        // 加载纹理
        if (isInintFinsh == false) {
            initTexture();
            isInintFinsh = true;
        }

        //制定使用某套着色器程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, matrixState.getFinalMatrix(), 0);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, matrixState.getMMatrix(), 0);
        //将光源位置传入着色器程序
        GLES20.glUniform3fv(maLightLocationHandle, 1, matrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(maCameraHandle, 1, matrixState.cameraFB);
        // 将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mVertexBuffer
                );
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maNormalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );
        // 颜色相关

        //为画笔指定顶点纹理坐标数据
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        // 材质alpha
        GLES20.glUniform1f(muOpacityHandle, mAlpha);
        // 启用顶点纹理数组
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //启用顶点位置、法向量、纹理坐标数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        //绘制加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

}
