package com.example.chh85.exercise_elderly;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Countdown {
    Button btnStart, btnPause;
    TextView tView;
    private boolean StartorResume = true;
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;
    private long timeRemaining = 0;
    int ex_num, ex_time;
    long minute, seconds;
    Context context;
    String dbattr="ex1_weeknum";
    String ex_name;
    public void setDbattr(String dbrecord){
        this.dbattr = dbrecord;
    }

    Countdown(Context context,Button btnStart, Button btnPause, TextView tview,int ex_name, int ex_num, int ex_time) {
        this.context = context;
        this.btnPause=btnPause;
        this.btnStart=btnStart;
        this.tView=tview;
        this.ex_num = ex_num;
        this.ex_time=ex_time;
        this.ex_name = Integer.toString(ex_name);
    }

    public void countdown_func() {

        btnPause.setEnabled(false);


        //Set a Click Listener for start button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StartorResume == true) {
                    isPaused = false;
                    isCanceled = false;

                    //Disable the start and pause button
                    //Enabled the pause and cancel button
                    btnPause.setEnabled(true);
                    btnStart.setEnabled(false);
                    CountDownTimer timer;
                    long millisInFuture = 10000;  //ex_time*60000; // 분을 초로..                                IMPORTANT YOU SHOUD CHANGE THIS SAMPLE!!!!
                    long countDownInterval = 1000; //1 second


                    //Initialize a new CountDownTimer instance
                    timer = new CountDownTimer(millisInFuture, countDownInterval) {
                        public void onTick(long millisUntilFinished) {
                            //do something in every tick

                            minute_seconds(millisUntilFinished);

                            if (isPaused || isCanceled) {
                                //If the user request to cancel or paused the
                                //CountDownTimer we will cancel the current instance
                                cancel();
                            } else {
                                //Display the remaining seconds to app interface
                                //1 second = 1000 milliseconds
                                tView.setText("" + minute + "분 " + seconds + "초");
                                //Put count down timer remaining time in a variable
                                timeRemaining = millisUntilFinished;
                            }
                        }

                        public void onFinish() {
                            //Do something when count down finished
                            tView.setText("운동 완료!");
                            updatedata();
                            //Enable the start button
                            btnStart.setEnabled(true);
                            //Disable the pause, resume and cancel button
                            btnPause.setEnabled(false);
                        }
                    }.start();
                }

                //resume일때
                if (StartorResume == false) {
                    btnStart.setEnabled(false);
                    //Enable the pause and cancel button
                    btnPause.setEnabled(true);
                    //Specify the current state is not paused and canceled.
                    isPaused = false;
                    isCanceled = false;

                    //Initialize a new CountDownTimer instance
                    long millisInFuture = timeRemaining;
                    long countDownInterval = 1000;

                    new CountDownTimer(millisInFuture, countDownInterval) {
                        public void onTick(long millisUntilFinished) {
                            minute_seconds(millisUntilFinished);

                            //Do something in every tick
                            if (isPaused || isCanceled) {
                                //If user requested to pause or cancel the count down timer
                                cancel();
                            } else {
                                tView.setText("" + minute + "분 " + seconds + "초");
                                //Put count down timer remaining time in a variable
                                timeRemaining = millisUntilFinished;
                            }
                        }

                        public void onFinish() {
                            //Do something when count down finished
                            tView.setText("운동 완료!");
                            updatedata();
                            //Disable the pause, resume and cancel button
                            btnPause.setEnabled(false);
                            //Enable the start button
                            btnStart.setText("시작");
                            StartorResume = true;
                            btnStart.setEnabled(true);

                        }
                    }.start();
                }
            }

        });

        //Set a Click Listener for pause button
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user request to pause the CountDownTimer
                isPaused = true;
                StartorResume = false;
                //Disable the start and pause button
                btnStart.setText("다시시작");
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
            }
        });


    }

    public void minute_seconds(long seconds_val) {
        minute = seconds_val / 60000;
        seconds = (seconds_val % 60000) / 1000;

    }

    Send_FB send_exinfo;

    public void updatedata(){
        send_exinfo = new Send_FB();
        ExerciseDB exerciseDB =new ExerciseDB(context,"exerciseDB",null,1);
        if(ex_num<1){
            ex_num=1;
        }
        exerciseDB.updateDB(exerciseDB,dbattr,ex_num-1);
        String tmp_time = Integer.toString(ex_time);
        send_exinfo.FB_exercise(ex_name,tmp_time);

    }
}

