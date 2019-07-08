package com.mxnavi.opengl4android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mxnavi.opengl4android.cube.CubeActivity;
import com.mxnavi.opengl4android.myobj.ObjActivity;
import com.mxnavi.opengl4android.plane.PlaneActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                Intent intent1 = new Intent(this, PlaneActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_2:
                Intent intent2 = new Intent(this, CubeActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_3:
                Intent intent3 = new Intent(this, ObjActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
