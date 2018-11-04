package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Exercise_act extends AppCompatActivity {

    ExerciseDB exerciseDB;
    UserDB userDB;

    //날짜 관련 변수들
    String StartDate=null;
    String EndDate = null;
    String UpdateDate = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    TextView ex_1,ex_2,ex_3,week_date;

    int updated;//메인 액티비티에서 갱신여부 알려주는 변수


    int[] exercise_list;
    int[] exercise_time;
    Date date = new Date();
    ScheduleExercise scheduleExercise = new ScheduleExercise();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("운동하기");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_act);

        //DB들
        exerciseDB = new ExerciseDB(this,"exerciseDB",null,1);
        userDB = new UserDB(this,"user_db",null,1);



        //메인에서 건너온 값들..반가워!
        Intent get_intent = getIntent();
        updated = get_intent.getIntExtra("updated",3);


        //운동 값 가져오기 업데이트 된거 안된거

        if(updated ==1){ //업데이트되었으면

            //운동스케쥴 계산, DB에 날짜 운동 스케쥴 저장
            UpdateSchedule();
        }

        //TextView

        ex_1 = (TextView) findViewById(R.id.ex1_num);
        ex_2 = (TextView) findViewById(R.id.ex2_num);
        ex_3 = (TextView) findViewById(R.id.ex3_num);
        week_date = (TextView) findViewById(R.id.week_date);

        //업데이트안되었으면
        //DB에서 값 가져오고 보여주기
        getDB_show();

        //시작날짜와 일주일후 끝나는 날짜 구하기.

        Start_End_date();

        //날짜가 지나면 다시 업데이트 해줘야함.
        boolean updateneed = Date_Update(); //true: 필요
        if(updateneed==true){
            UpdateSchedule();
        }
        //업데이트한 날짜 다시 계산.
        Start_End_date();


        String date_show = StartDate+"~"+EndDate;
        week_date.setText(date_show);





        //Button clicked
        //Exercise1
        ImageButton exercise1_btn,exercise2_btn,exercise3_btn;
        exercise1_btn = (ImageButton) findViewById(R.id.exercise1_btn);
        exercise2_btn=(ImageButton)findViewById(R.id.exercise2_btn);
        exercise3_btn = (ImageButton) findViewById(R.id.exercise3_btn);
        exercise1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] ex1_info = {exercise_list[0],exercise_time[0]}; //운동1 횟수, 시간 intent로
                Intent exercise1_intent = new Intent(getApplicationContext(),Exercise1_act.class);
                exercise1_intent.putExtra("ex1",ex1_info);      //운동1 횟수, 시간 보내기
                startActivity(exercise1_intent);
            }
        });
        exercise2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] ex2_info = {exercise_list[1],exercise_time[1]};
                Intent exercise2_intent = new Intent(getApplicationContext(),Exercise2_act.class);
                exercise2_intent.putExtra("ex2",ex2_info);      //운동2 횟수, 시간 보내기
                startActivity(exercise2_intent);
            }
        });

        exercise3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] ex3_info = {exercise_list[2],exercise_time[2]};
                Intent exercise3_intent = new Intent(getApplicationContext(),Exercise3_act.class);
                exercise3_intent.putExtra("ex3",ex3_info);      //운동2 횟수, 시간 보내기
                startActivity(exercise3_intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDB_show();


    }


    public void UpdateSchedule(){
        //운동스케쥴 계산
        ArrayList<String> schedule_list;
        schedule_list = userDB.readDB(userDB);
        exercise_list=scheduleExercise.schedule_exercise(schedule_list.get(3),schedule_list.get(4),schedule_list.get(5));
        exercise_time=scheduleExercise.schedule_time();

        //시작날짜를 현재 시간으로 저장.

        StartDate = sdf.format(date);
        System.out.println("DB저장! "+ StartDate.toString());

        //DB 값 저장
        exerciseDB.insertDB(exerciseDB,StartDate.toString(),exercise_list[0],exercise_list[1],exercise_list[2],exercise_time[0],exercise_time[1],exercise_time[2]);


        //읽기테스트
        ArrayList<String> test_list = new ArrayList<>();
        test_list=exerciseDB.readDB(exerciseDB);
    }


    public void getDB_show(){
        //업데이트안되었으면 (액티비티 열릴때마다) 운동하고 오면 갱신되어야 하니까
        //DB에서 값 가져오고 보여주기
        ArrayList<String> exercise_DB = new ArrayList<>();
        exercise_DB=exerciseDB.readDB(exerciseDB);
        int[] dbtoint = {Integer.parseInt(exercise_DB.get(1)),Integer.parseInt(exercise_DB.get(2)),Integer.parseInt(exercise_DB.get(3))};
        int[] dbtoint2={Integer.parseInt(exercise_DB.get(4)),Integer.parseInt(exercise_DB.get(5)),Integer.parseInt(exercise_DB.get(6))};
        exercise_list = dbtoint;
        exercise_time=dbtoint2;
        StartDate = exercise_DB.get(0);
        System.out.println("EX1 NUM "+exercise_list[0]);

        ex_1.setText(Integer.toString(exercise_list[0]));
        ex_2.setText(Integer.toString(exercise_list[1]));
        ex_3.setText(Integer.toString(exercise_list[2]));
    }

    private void Start_End_date(){
        //시작날짜와 일주일후 끝나는 날짜 구하기. String StartDate, EndDate
        try {
            Date startdate_cal = new SimpleDateFormat("yyyy-MM-dd").parse(StartDate);
            System.out.println("date2 :   "+sdf.format(startdate_cal));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate_cal);
            calendar.add(Calendar.DAY_OF_MONTH,7);
            Date enddate_cal = calendar.getTime();
            EndDate = sdf.format(enddate_cal);
            System.out.println("시작날짜: "+StartDate+"끝 날짜"+EndDate);
        } catch (ParseException e) {
            System.out.println("ERRRROORRR!1");
            e.printStackTrace();
        }
    }


    private boolean Date_Update(){
        String cur_date = sdf.format(date);
        System.out.println(cur_date);

        int cur_date_tmp, end_date_tmp;
        cur_date_tmp= Integer.parseInt(cur_date.replaceAll("-",""));
        end_date_tmp=Integer.parseInt(EndDate.replaceAll("-",""));

        if(cur_date_tmp>end_date_tmp){
            System.out.println("날짜 업데이트 필요");

            return  true;
        } else {
            System.out.println("아직 마지막날이 아님");
            return false;
        }

    }
}
