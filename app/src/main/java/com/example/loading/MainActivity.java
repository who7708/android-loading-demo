package com.example.loading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loading.activies.MvpActivity;
import com.example.loading.activies.MvpActivity2;

/**
 * @author Chris
 * @version 1.0.0
 * @since 2021/02/20
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mvp_btn).setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.mvp_btn == v.getId()) {
            startOurActivity(MvpActivity.class);
        } else if (R.id.mvp_btn == v.getId()) {
            startOurActivity(MvpActivity2.class);
        }
    }

    private void startOurActivity(Class<?> clazz) {
        Intent intent = new Intent(MainActivity.this, clazz);
        intent.putExtra("key", "value");
        MainActivity.this.startActivity(intent);
    }
}