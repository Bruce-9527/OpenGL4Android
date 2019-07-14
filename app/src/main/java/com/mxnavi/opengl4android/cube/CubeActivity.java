package com.mxnavi.opengl4android.cube;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mxnavi.opengl4android.R;
import com.mxnavi.opengl4android.plane.PlaneGlRenderer;
import com.mxnavi.opengl4android.plane.PlaneGlSurfaceView;

/**
 * 立体几何图形
 */
public class CubeActivity extends AppCompatActivity {

    private CubeRenderer renderer;
    private PlaneGlSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube);
        glSurfaceView = (PlaneGlSurfaceView) findViewById(R.id.glsv_plane);
        renderer = new CubeRenderer();
        glSurfaceView.setRenderer(renderer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        glSurfaceView.onPause();
        return super.onMenuOpened(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cube:
                // 正方体
                renderer.setShape(new Cube());
                break;
            case R.id.cylinder:
                // 圆柱体
                renderer.setShape(new Cylinder(glSurfaceView));
                break;
            case R.id.sphere:
                // 球体
                renderer.setShape(new Ball(glSurfaceView));
                break;

            default:
                break;

        }
        glSurfaceView.onResume();
        return super.onOptionsItemSelected(item);
    }


}
