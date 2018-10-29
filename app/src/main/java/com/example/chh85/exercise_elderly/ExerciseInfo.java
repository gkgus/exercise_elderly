package com.example.chh85.exercise_elderly;

import java.io.Serializable;

public class ExerciseInfo implements Serializable {
    public int drawableId;
    public String exercise_name;

    public ExerciseInfo(int drawableId, String exercise_name){
        this.drawableId=drawableId;
        this.exercise_name= exercise_name;
    }
}
