package com.example.lvpeiling.nodddle.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.lvpeiling.nodddle.ActivitiesCollector;
import com.example.lvpeiling.nodddle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.splash_lottie)
    LottieAnimationView splashLottie;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitiesCollector.addActivity(this);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        unbinder = ButterKnife.bind(this);
        splashLottie.setImageAssetsFolder("images/");
        splashLottie.setAnimation("data.json");
        splashLottie.loop(false);
        splashLottie.playAnimation();
        splashLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivitiesCollector.removeActivity(this);
    }
}
