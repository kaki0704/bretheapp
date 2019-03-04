package com.example.breathe;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.breathe.util.Prefs;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView breathsTxt, timeTxt, sessionTxt, guideTxt;
    private Button startButton;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.lotusImage);
        breathsTxt = findViewById(R.id.breathetakenztxt);
        timeTxt = findViewById(R.id.lastBreathTxt);
        sessionTxt = findViewById(R.id.todayMinutesTxt);
        guideTxt = findViewById(R.id.guideTxt);
        prefs = new Prefs(this);

        startIntroAnimation();

        sessionTxt.setText(MessageFormat.format("{0} min today", prefs.getSessions()));
        breathsTxt.setText(MessageFormat.format("{0} Breaths", prefs.getBreaths()));
        timeTxt.setText(prefs.getDate());

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });


        ViewAnimator
                .animate(imageView)
                .translationY(-1000, 0)
                .alpha(0,1)
                //.andAnimate(text)
                .dp().translationX(-20, 0)
                .decelerate()
                .duration(2000)
                .thenAnimate(imageView)
                .scale(1f, 0.5f, 1f)
                .rotation(270)
                .repeatCount(3)
                .accelerate()
                .duration(2000)
                .start();
    }

    private void startIntroAnimation(){
        ViewAnimator
                .animate(guideTxt)
                .scale(0, 1)
                .duration(1500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTxt.setText("Breathe");
                    }
                })
                .start();

    }

    private void startAnimation(){
        ViewAnimator
                .animate(imageView)
                .alpha(0, 1)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTxt.setText("Inhale... Exhale");
                    }
                })
                .decelerate()
                .duration(1000)
                .thenAnimate(imageView)
                .scale(0.02f, 1.5f, 0.02f)
                .rotation(360)
                .repeatCount(5)
                .accelerate()
                .duration(5000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        guideTxt.setText("Good job!");
                        imageView.setScaleX(1.0f);
                        imageView.setScaleY(1.0f);

                        prefs.setSessions(prefs.getSessions() + 1);
                        prefs.setBreaths(prefs.getBreaths() + 1);
                        prefs.setDate(System.currentTimeMillis());

                        //refresh Activity

                        new CountDownTimer(2000,1000){

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(getApplicationContext(), MainActivity
                                        .class));
                                finish();
                            }
                        }.start();

                    }
                })
                .start();

    }
}
