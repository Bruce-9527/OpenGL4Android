package com.mxnavi.opengl4android.myobj;

import android.graphics.Bitmap;
import android.text.TextUtils;


import com.mxnavi.opengl4android.base.BitmapUtil;
import com.mxnavi.opengl4android.base.GLEntity;
import com.mxnavi.opengl4android.base.GLObjColorEntity;
import com.mxnavi.opengl4android.base.GLObject;
import com.mxnavi.opengl4android.base.MatrixState;
import com.mxnavi.opengl4android.base.ObjInfo;
import com.mxnavi.opengl4android.base.ObjLoaderUtil;
import com.mxnavi.opengl4android.plane.PlaneGlSurfaceView;

import java.util.ArrayList;

/**
 *
 */
public class GokuGroup extends GLObject {
    private static final String TAG = GokuGroup.class.getSimpleName();


    private ArrayList<ObjInfo> objDatas;
    private ArrayList<GLEntity> mObjSprites = new ArrayList<GLEntity>();

    public GokuGroup(PlaneGlSurfaceView scene) {
        super(scene);
        /**
         * ----勋章---
         */
        try {
            objDatas = ObjLoaderUtil.load("redcar.obj", scene.getResources());
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initObjs() {
        mObjSprites.clear();
        if (objDatas != null) {
            for (int i = 0; i < objDatas.size(); i++) {
                ObjInfo data = objDatas.get(i);
                //
                int diffuseColor = data.mtlData != null ? data.mtlData.Kd_Color : 0xffffffff;
                float alpha = data.mtlData != null ? data.mtlData.alpha : 1.0f;
                String texturePath = data.mtlData != null ? data.mtlData.Kd_Texture : "";

                // 构造对象
                if (data.aTexCoords != null && data.aTexCoords.length != 0 && TextUtils.isEmpty(texturePath) == false) {
                    Bitmap bmp = BitmapUtil.getBitmapFromAsset(getBaseScene().getContext(), texturePath);
                    GLEntity spirit = new GokuEntity(getBaseScene(), data.aVertices, data.aNormals, data.aTexCoords, alpha, bmp);
                    mObjSprites.add(spirit);
                } else {
                    GLEntity spirit = new GLObjColorEntity(getBaseScene(), data.aVertices, data.aNormals, diffuseColor, alpha);
                    mObjSprites.add(spirit);
                }
            }
        }
    }

    private void init() {
        mSpriteScale = 5f;
        // alpha数值
        mSpriteAlpha = 1;
        // 旋转
        mSpriteAngleX = -90f;
        mSpriteAngleY = 0;
        mSpriteAngleZ = 0;
    }


    @Override
    public void onDraw(MatrixState matrixState) {
        super.onDraw(matrixState);
        matrixState.scale(getSpriteScale(), getSpriteScale(), getSpriteScale());
//         旋转
        matrixState.rotate(this.getSpriteAngleY(), 0, 1, 0);
        matrixState.rotate(this.getSpriteAngleX(), 1, 0, 0);
        // 绘制
        for (int i = 0; i < mObjSprites.size(); i++) {
            GLEntity sprite = mObjSprites.get(i);
            sprite.onDraw(matrixState);
        }
    }

}