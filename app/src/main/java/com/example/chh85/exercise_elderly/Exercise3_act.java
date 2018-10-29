package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Exercise3_act extends AppCompatActivity {
    int ex3_num;
    int ex3_time;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise3_act);

        TextView tView = (TextView) findViewById(R.id.ex3_tv);
        Button btnStart = (Button) findViewById(R.id.ex3_start);
        Button btnPause = (Button) findViewById(R.id.ex3_pause);

        int[] ex3_list;
        Intent get_intent = getIntent();
        ex3_list=get_intent.getIntArrayExtra("ex3"); //Exercise_act에서 운동2 횟수, 시간 받음
        ex3_num = ex3_list[0];
        ex3_time= ex3_list[1];


        Countdown ex2_countdown = new Countdown(getApplicationContext(),btnStart,btnPause,tView,3,ex3_num,ex3_time);
        ex2_countdown.setDbattr("ex3_weeknum"); //DB에서 레코드 찾아서 바꾸기
        ex2_countdown.countdown_func();


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final String[] exercise3title = {"누워서 무릎 가슴으로 당기기","누워서 무릎 펴서 가슴으로 당기기","허리 숙여 발목 잡기",
                "허리 숙여 발목 잡기","넙다리 안쪽 늘리기","옆으로 누워서 발 뒤로 당기기"};

        final ArrayList<ExerciseInfo> exerciselist3 = new ArrayList<>();
        exerciselist3.add( new ExerciseInfo(R.drawable.ex3_1,exercise3title[0]));
        exerciselist3.add(new ExerciseInfo(R.drawable.ex3_2,exercise3title[1]));
        exerciselist3.add(new ExerciseInfo(R.drawable.ex3_3,exercise3title[2]));
        exerciselist3.add(new ExerciseInfo(R.drawable.ex3_4,exercise3title[3]));
        exerciselist3.add(new ExerciseInfo(R.drawable.ex3_5,exercise3title[4]));
        exerciselist3.add(new ExerciseInfo(R.drawable.ex3_6,exercise3title[5]));


        MyAdapter myAdapter = new MyAdapter(exerciselist3);

        mRecyclerView.setAdapter(myAdapter);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {

                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent exercise3_intent = new Intent(getApplicationContext(),Exercise_list_act.class);
                        ExerciseInfo exerciseInfo = exerciselist3.get(position);
                        exercise3_intent.putExtra("list_info", exerciseInfo);
                        startActivity(exercise3_intent);
                    }
                }
        );


    }
}
