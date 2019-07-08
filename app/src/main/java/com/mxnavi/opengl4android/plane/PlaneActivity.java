package com.mxnavi.opengl4android.plane;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mxnavi.opengl4android.R;

/**
 * 平面activity
 */
public class PlaneActivity extends AppCompatActivity {

    private PlaneGlRenderer renderer;
    private PlaneGlSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane);
        glSurfaceView = (PlaneGlSurfaceView) findViewById(R.id.glsv_plane);
        renderer = new PlaneGlRenderer();
        glSurfaceView.setRenderer(renderer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plane_menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        glSurfaceView.onPause();
        return super.onMenuOpened(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.triangle:
                renderer.setShape(new Triangle());
                break;
            case R.id.square:
                renderer.setShape(new Square());
                break;
            case R.id.circle:
                renderer.setShape(new Circle());
                break;
            default:
                break;

        }
        glSurfaceView.onResume();
        return super.onOptionsItemSelected(item);
    }


}
