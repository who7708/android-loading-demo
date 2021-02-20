package com.example.loading.activies;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loading.R;
import com.example.loading.presenter.StudentPresenter;
import com.example.loading.view.StudentView;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * @author Chris
 * @version 1.0.0
 * @since 2021/02/20
 */
public class MvpActivity2 extends AppCompatActivity implements StudentView {

    private TextView mShowResult;
    private Button mRequestBtn;

    private StudentPresenter studentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

        KProgressHUD kProgressHUD = KProgressHUD.create(MvpActivity2.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                // .setLabel("请等待")
                .setDetailsLabel("数据正在加载...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                // 大小
                // .setSize(100, 100)
                .setBackgroundColor(R.color.teal_200)
                .setDimAmount(0.5f);

        studentPresenter = new StudentPresenter(MvpActivity2.this, kProgressHUD);

        // 绑定 view 对象
        studentPresenter.attachView(MvpActivity2.this);

        mShowResult = findViewById(R.id.show_result);
        mRequestBtn = findViewById(R.id.request_btn);

        mRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 展示数据
                studentPresenter.getData((int) (Math.random() * 100) + 1);
            }
        });
    }

    @Override
    public void show(String msg) {
        mShowResult.setText(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        studentPresenter.detachView();
    }
}