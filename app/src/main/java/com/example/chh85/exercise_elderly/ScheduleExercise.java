package com.example.chh85.exercise_elderly;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScheduleExercise {
    int fat;

    ArrayList <int[]> exercise_int = new ArrayList<int[]>();

    public int[] schedule_exercise(String height, String weight, String disease_list){
        this.fat=fat_cal(Float.parseFloat(height),Float.parseFloat(weight)); //bmi로 정상,과체중,비만 측정
        exercise_int.clear();
        if(disease_list.isEmpty()) {
        }
        else{
            if(disease_list.contains("h")){
            int[] high_blood = {3,2,0};
            exercise_int.add(high_blood);


        }
        if(disease_list.contains("g")){
            int[] guanjeol = {0,3,3};
            exercise_int.add(guanjeol);
        }
        if(disease_list.contains("d")){
            int[] diabetes = {3,2,0};
            exercise_int.add(diabetes);
        }
        if(disease_list.contains("j")){
            int[] jihyeol = {5,2,2};
            exercise_int.add(jihyeol);
        }

        }
        if(fat==0){
            int[] normal = {2,2,2};
            exercise_int.add(normal);
        }
        if(fat==1){
            int[] overweight={3,2,5};
            exercise_int.add(overweight);
        }
        if(fat==2){
            int[] fat = {5,2,5};

            exercise_int.add(fat);
        }
        int ex1_min=7;
        int ex2_max=0;
        int ex3_max=0;
        for (int[] exercises:exercise_int) {

            if(exercises[0]<ex1_min){
                ex1_min=exercises[0];
            }
            if(exercises[1]>ex2_max){
                ex2_max=exercises[1];
            }
            if(exercises[1]>ex3_max){
                ex3_max=exercises[1];
            }
        }
        int[] final_exercise ={ex1_min,ex2_max,ex3_max};
        System.out.println("=================="+ex1_min+ex2_max+ex3_max);

        return  final_exercise;

    }

    public int[] schedule_time (){
        int[] exercise_time={30,15,15};

        return exercise_time;
    }

    private Integer fat_cal(float height, float weight){
        int fat_tmp=0;
        float bmi;
        bmi= weight/((height/100)*(height/100));

        if(bmi<=23.0){
            fat_tmp=0;
        }
        if(bmi >23.0 && bmi<25.0){  //과체중
            fat_tmp=1;
        }
        if(bmi>=25.0){
            fat_tmp=2;
        }

        return fat_tmp;
    }
}
