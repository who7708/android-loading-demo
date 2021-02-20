package com.example.loading.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.loading.model.ExceptionHandle;
import com.example.loading.model.Student;
import com.example.loading.service.StudentService;
import com.example.loading.utils.Retrofit2Helper;
import com.example.loading.view.StudentView;
import com.kaopiz.kprogresshud.KProgressHUD;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Chris
 * @version 1.0.0
 * @since 2021/02/21
 */
public class StudentPresenter {

    private static final String TAG = StudentPresenter.class.getName();

    private StudentView studentView;

    private final StudentService studentService;

    private Context context;

    private KProgressHUD kProgressHUD;

    public StudentPresenter(Context context, KProgressHUD kProgressHUD) {
        this.context = context;
        this.kProgressHUD = kProgressHUD;
        this.studentService = Retrofit2Helper.getInstance().createService(StudentService.class);
    }

    public void attachView(StudentView studentView) {
        this.studentView = studentView;
    }

    public void detachView() {
        this.studentView = null;
    }

    public void getData(int id) {
        studentService.detail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Student>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe: " + Thread.currentThread().getName());
                        if (!kProgressHUD.isShowing()) {
                            kProgressHUD.show();
                        }
                    }

                    @Override
                    public void onNext(@NonNull Student student) {
                        Log.i(TAG, "onNext: " + Thread.currentThread().getName());
                        String name = student.getName();
                        int age = student.getAge();
                        studentView.show("姓名：" + name + ", 年龄：" + age);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: " + Thread.currentThread().getName());
                        if (kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                        //对网络请求错误统一封装处理
                        ExceptionHandle.ResponseThrowable error = ExceptionHandle.handleException(e);
                        Toast.makeText(context, error.getCode() + ":" + error.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: " + Thread.currentThread().getName());
                        if (kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                    }
                });
        Log.i(TAG, "getData: 请求完成");
    }

}
