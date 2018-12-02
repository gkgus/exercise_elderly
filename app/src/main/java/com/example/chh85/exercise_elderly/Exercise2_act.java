package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Exercise2_act extends AppCompatActivity {
    int ex2_num;
    int ex2_time;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("근력 운동");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise2_act);
        TextView tView = (TextView) findViewById(R.id.ex2_tv);
        Button btnStart = (Button) findViewById(R.id.ex2_start);
        Button btnPause = (Button) findViewById(R.id.ex2_pause);

        int[] ex2_list;
        Intent get_intent = getIntent();
        ex2_list=get_intent.getIntArrayExtra("ex2"); //Exercise_act에서 운동2 횟수, 시간 받음
        ex2_num = ex2_list[0];
        ex2_time= ex2_list[1];

        tView.setText("근력 운동 "+ex2_time+"분");
        Countdown ex2_countdown = new Countdown(getApplicationContext(),btnStart,btnPause,tView,2,ex2_num,ex2_time);
        ex2_countdown.setDbattr("ex2_weeknum"); //DB에서 레코드 찾아서 바꾸기
        ex2_countdown.countdown_func();


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final String[] exercise2title = {"넙다리 네갈래근 힘주기","엎드려서 다리 들어 올리기","누워서 다리 들어 올리기",
                "옆으로 누워서 다리 들어 올리기","앉아서 엉덩이 관절 굽히기","의자에 앉아 다리 뻗어 올리기"};
        final String[] exercise2_how ={"(좌/우) 무릎 아래에 접은 수건을 놓고 발을 몸 쪽으로 당겨줍니다.","엎드린 상태에서 두다리를 들어 올려줍니다.",
                "(좌/우) 누운 상태에서 상체를 들어올리고 다리를 들어 올립니다.","(좌/우) 옆으로 누운 상태에서 다리를 들어 올립니다.",
                "(좌/우) 앉은 상태에서 한 쪽 다리를 들어 올립니다.","(좌/우) 의자에 앉아 한쪽 다리를 뻗어 올립니다."};
        final ArrayList<ExerciseInfo> exercise2list = new ArrayList<>();


        exercise2list.add( new ExerciseInfo(R.drawable.ex2_1,exercise2title[0],exercise2_how[0]));
        exercise2list.add(new ExerciseInfo(R.drawable.ex2_2,exercise2title[1],exercise2_how[1]));
        exercise2list.add(new ExerciseInfo(R.drawable.ex2_3,exercise2title[2],exercise2_how[2]));
        exercise2list.add(new ExerciseInfo(R.drawable.ex2_4,exercise2title[3],exercise2_how[3]));
        exercise2list.add(new ExerciseInfo(R.drawable.ex2_5,exercise2title[4],exercise2_how[4]));
        exercise2list.add(new ExerciseInfo(R.drawable.ex2_6,exercise2title[5],exercise2_how[5]));


        MyAdapter myAdapter = new MyAdapter(exercise2list);

        mRecyclerView.setAdapter(myAdapter);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {

                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent exercise2_intent = new Intent(getApplicationContext(),Exercise_list_act.class);
                        ExerciseInfo exerciseInfo = exercise2list.get(position);
                        exercise2_intent.putExtra("list_info", exerciseInfo);
                        startActivity(exercise2_intent);
                    }
                }
        );


    }
}
