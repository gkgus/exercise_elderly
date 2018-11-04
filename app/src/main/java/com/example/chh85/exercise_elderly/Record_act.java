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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Record_act extends AppCompatActivity {

    private DatabaseReference mRead_FB;
    String mUserID = Send_FB.getmUserID();
    RecordDB recordDB;
    ArrayList<String> date_exerciselist = new ArrayList<String>();
    Handler mHandler = null;
    HashSet<String> date_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("기록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_act);
        recordDB = new RecordDB(this,"record_exercise",null,1);


        final MaterialCalendarView mcv = findViewById(R.id.calendarView);

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2018, 1, 1))
                .setMaximumDate(CalendarDay.from(2020, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mcv.addDecorators();

        denote_date(mcv);

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String select_date = date.toString();
                select_date = select_date.replaceFirst("CalendarDay\\{","");
                select_date = select_date.replaceFirst("\\}","");
                date_exerciselist =recordDB.date_exercise(recordDB,select_date);
                Dialog(select_date);
            }
        });


        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(selected_date);
        //calendar.add(Calendar.DATE, 5);
        //CalendarDay day = CalendarDay.from(calendar);
        //dates.add(day);
    }

    public void denote_date (MaterialCalendarView mcv) {

        date_set= recordDB.set_of_date(recordDB);
        ArrayList<CalendarDay> dates = new ArrayList<>();
        Calendar ex_calendar = Calendar.getInstance();
        CalendarDay day;
        for(String element: date_set){
            try {
                Date exercise_date = new SimpleDateFormat("yyyy-MM-dd").parse(element);
                ex_calendar.setTime(exercise_date);
                day = CalendarDay.from(ex_calendar);
                dates.add(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mcv.addDecorators(new EventDecorator(Color.RED,dates));
    }

    public void Dialog(String select_date){
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
