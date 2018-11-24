package com.example.chh85.exercise_elderly;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class Record_act extends AppCompatActivity {


    RecordDB recordDB;
    ArrayList<String> date_exerciselist = new ArrayList<String>();

    HashSet<String> date_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("기록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_act);
        recordDB = new RecordDB(this,"record_exercise",null,1);

        final TextView YearMonthTv;
        final ExpCalendarView calendarView = ((ExpCalendarView) findViewById(R.id.calendar));
        YearMonthTv = (TextView) findViewById(R.id.main_YYMM_Tv);
        YearMonthTv.setText(Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월");
        calendarView.measureCurrentView(0);
        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(String.format("%d년 %d월", year, month));
            }
        });
        calendarView.setMarkedStyle(MarkStyle.BACKGROUND, Color.parseColor("#FFBA55DB"));
        denote_date(calendarView);
        //calendarView.markDate(new DateData(2018,11,21));

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String getDay;
                if(date.getDay()<10){
                    getDay= "0"+Integer.toString(date.getDay());
                } else {
                    getDay = Integer.toString(date.getDay());
                }
                String select_date= date.getYear()+"-"+date.getMonth()+"-"+getDay;
                Dialog(select_date);
            }
        });

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String cur_month = sdf.format(date);
        int ex1_count = recordDB.count_ex(recordDB,1,cur_month);
        int ex2_count = recordDB.count_ex(recordDB,2,cur_month);
        int ex3_count = recordDB.count_ex(recordDB,3,cur_month);
        System.out.println("ex1 월 횟수>>"+ex1_count);
        TextView ex1_count_tv, ex2_count_tv, ex3_count_tv;
        ex1_count_tv = (TextView) findViewById(R.id.ex1_count);
        ex2_count_tv = (TextView) findViewById(R.id.ex2_count);
        ex3_count_tv = (TextView) findViewById(R.id.ex3_count);

        ex1_count_tv.setText("유산소 운동: "+ex1_count);
        ex2_count_tv.setText("근력 운동: "+ex2_count);
        ex3_count_tv.setText("스트레칭 운동: "+ex3_count);


        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(selected_date);
        //calendar.add(Calendar.DATE, 5);
        //CalendarDay day = CalendarDay.from(calendar);
        //dates.add(day);
    }

    public void denote_date (ExpCalendarView cv) {

        date_set= recordDB.set_of_date(recordDB);

        for(String element: date_set){

            System.out.println(element);

            int year= Integer.parseInt(element.substring(0,4));
            int month=Integer.parseInt(element.substring(5,7));
            int day = Integer.parseInt(element.substring(8,10));

            cv.markDate(year,month,day);

        }


    }

    public void Dialog(String select_date){

        date_exerciselist=recordDB.date_exercise(recordDB,select_date);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(select_date);
        String contents="\n";
        if(date_exerciselist.size()>0){
            for(int i=0;i<date_exerciselist.size();i++){
                if(i%2==0){
                    switch (date_exerciselist.get(i)){
                        case "1":
                            contents= contents.concat("유산소운동: ");
                            break;
                        case "2":
                            contents= contents.concat("근력운동: ");
                            break;
                        case "3":
                            contents= contents.concat("스트레칭: ");
                            break;
                    }
                } else{
                    contents= contents.concat(date_exerciselist.get(i)+"분"+"\n");
                }
            }
            builder.setMessage(contents);
        } else{
            builder.setMessage("운동 기록이 없습니다.");
        }

        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.show();
    }

/*
    public void getFB() {
        mRead_FB= FirebaseDatabase.getInstance().getReference().child(mUserID).child("exerciserec");
        final ArrayList<String> ex_list = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String tmpkey = childDataSnapshot.getKey();
                   // System.out.println("KEY"+tmpkey);
                   // System.out.println(""+ childDataSnapshot.child("date").getValue());   //gives the value for given keyname
                    ex_list.add(childDataSnapshot.child("date").getValue().toString());
                    ex_list.add(childDataSnapshot.child("exercise_name").getValue().toString());
                    ex_list.add(childDataSnapshot.child("time").getValue().toString());
                    System.out.println("====="+ex_list.get(0)+" "+ex_list.get(1)+" "+ex_list.get(2));
                    recordDB.insertDB(recordDB,ex_list.get(0),ex_list.get(1),ex_list.get(2));
                    ex_list.clear();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                System.out.println("==========DB ERROR============");
            }
        };

        mRead_FB.addListenerForSingleValueEvent(postListener);

    }
*/





}
