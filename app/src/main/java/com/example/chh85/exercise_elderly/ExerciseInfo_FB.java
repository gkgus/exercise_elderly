package com.example.chh85.exercise_elderly;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseInfo_FB {
    public String date, exercise_name, time;

    public ExerciseInfo_FB(){

    }

    public ExerciseInfo_FB( String date,String exercise_name, String time){
        this.date = date;
        this.exercise_name = exercise_name;
        this.time=time;
    }

    public String getExercise_name() {
        return exercise_name;
    }
}
