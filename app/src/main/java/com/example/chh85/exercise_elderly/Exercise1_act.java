package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Exercise1_act extends AppCompatActivity {
    int ex1_num, ex1_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("유산소 운동");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise1_act);


        int[] ex1_list;
        Intent get_intent = getIntent();
        ex1_list = get_intent.getIntArrayExtra("ex1");
        ex1_num = ex1_list[0];
        ex1_time = ex1_list[1];


        //Get reference of the XML layout's widgets
        final TextView tView = (TextView) findViewById(R.id.tv);
        final Button btnStart = (Button) findViewById(R.id.btn_start);
        final Button btnPause = (Button) findViewById(R.id.btn_pause);

        Countdown ex2_countdown = new Countdown(getApplicationContext(),btnStart,btnPause,tView,1,ex1_num,ex1_time);
        ex2_countdown.setDbattr("ex1_weeknum"); //DB에서 속성 찾아서 바꾸기
        ex2_countdown.countdown_func();

    }

}
