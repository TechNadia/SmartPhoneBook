package com.example.pc21.phonebook;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends Activity implements Animation.AnimationListener {

    MediaPlayer mediaPlayer;

    Thread background;

    ImageView ivCircle1, ivCircle2,ivCircle3;
    static Animation rotateAnimation1;
    static Animation rotateAnimation2;
    static Animation rotateAnimation3;

    static Animation rotateAnimation4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivCircle1 = (ImageView) findViewById(R.id.ivCircle1);
        ivCircle2 = (ImageView) findViewById(R.id.ivCircle2);
        ivCircle3 = (ImageView) findViewById(R.id.ivCircle3);
        rotateAnimation1 = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_start);
        rotateAnimation2 = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_start);
        rotateAnimation3 = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_start);

        rotateAnimation4 = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_end);


        mediaPlayer=MediaPlayer.create(this,R.raw.corrs);
        mediaPlayer.start();

        ivCircle1.setVisibility(View.VISIBLE);

        ivCircle1.startAnimation(rotateAnimation1);
        rotateAnimation1.setAnimationListener(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
        mediaPlayer.release();
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        ivCircle2.startAnimation(rotateAnimation2);
        rotateAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivCircle3.startAnimation(rotateAnimation3);
                rotateAnimation3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivCircle1.startAnimation(rotateAnimation1);
                        ivCircle3.startAnimation(rotateAnimation4);
                        ivCircle2.startAnimation(rotateAnimation4);
                        ivCircle1.startAnimation(rotateAnimation4);
                        rotateAnimation4.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
