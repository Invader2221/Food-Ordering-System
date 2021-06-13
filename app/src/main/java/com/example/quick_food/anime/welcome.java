package com.example.quick_food.anime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.quick_food.Login;
import com.example.quick_food.R;

import static java.lang.Thread.sleep;

public class welcome extends AppCompatActivity {

    ImageView welcomeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeImage=(ImageView) findViewById(R.id.imagewelcome);

        Animation welcomed= AnimationUtils.loadAnimation(this,R.anim.welcomeanimation);
        welcomeImage.startAnimation(welcomed);

        Thread myTread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(3000);

                    Intent i=new Intent(welcome.this, Login.class);
                    startActivity(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        myTread.start();

    }

}
