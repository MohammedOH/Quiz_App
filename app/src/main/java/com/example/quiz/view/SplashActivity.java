package com.example.quiz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.quiz.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000;
    private ImageView logo;
    final Handler handler = new Handler();

    final Runnable runnable = new Runnable() {
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.splash_image_view);

        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.logo_animation);
        animator.setTarget(logo);
        ValueAnimator sizeAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.size_animation);
        sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
                logo.setLayoutParams(params);
            }
        });
        sizeAnimator.setTarget(logo);
        sizeAnimator.start();
        animator.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        finish();
    }

}
