package com.mxnavi.opengl4android.myobj;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mxnavi.opengl4android.R;
import com.mxnavi.opengl4android.plane.PlaneGlSurfaceView;

public class ObjActivity extends AppCompatActivity {

    private PlaneGlSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_obj);
        mGLView = (PlaneGlSurfaceView) findViewById(R.id.glsv_plane);
        GokuRenderer gokuRenderer = new GokuRenderer(mGLView);
        mGLView.setRenderer(gokuRenderer);
        // 渲染模式(被动渲染)
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGLView.setOnTouchListener(gokuRenderer.getTouchEventListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

}
