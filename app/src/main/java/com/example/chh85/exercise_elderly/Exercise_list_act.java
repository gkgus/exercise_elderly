package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Exercise_list_act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list_act);
        TextView textView = (TextView)findViewById(R.id.ex2_title);
        TextView exercise_how = (TextView) findViewById(R.id.exercise_how);
        ImageView imgView = (ImageView) findViewById(R.id.ex2_img);
        Intent getintent = getIntent();
        ExerciseInfo exerciseInfo;
        exerciseInfo =(ExerciseInfo) getintent.getSerializableExtra("list_info");

        imgView.setImageResource(exerciseInfo.drawableId);
        textView.setText(exerciseInfo.exercise_name);
        exercise_how.setText(exerciseInfo.exercise_how);
    }
}
