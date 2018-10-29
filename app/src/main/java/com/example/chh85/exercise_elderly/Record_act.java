package com.example.chh85.exercise_elderly;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import java.util.Map;

public class Record_act extends AppCompatActivity {
    private DatabaseReference mRead_FB;
    String mUserID = Send_FB.getmUserID();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_act);

        getFB();

        MaterialCalendarView mcv = findViewById(R.id.calendarView);

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mcv.addDecorators();

        String tmpdate= "2018-10-29";
        ArrayList<CalendarDay> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        try {
            Date selected_date = new SimpleDateFormat("yyyy-MM-dd").parse(tmpdate);

            calendar.setTime(selected_date);
            CalendarDay day = CalendarDay.from(calendar);
            dates.add(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

            }
        });
        mcv.addDecorators(new EventDecorator(Color.RED,dates));
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(selected_date);
        //calendar.add(Calendar.DATE, 5);
        //CalendarDay day = CalendarDay.from(calendar);
        //dates.add(day);




    }

    public void getFB(){
        mRead_FB= FirebaseDatabase.getInstance().getReference().child(mUserID).child("exerciserec");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String tmpkey = childDataSnapshot.getKey();
                    System.out.println("KEY"+tmpkey);
                    System.out.println(""+ childDataSnapshot.child("date").getValue());   //gives the value for given keyname
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
}
