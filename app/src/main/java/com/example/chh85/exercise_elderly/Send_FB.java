package com.example.chh85.exercise_elderly;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Send_FB {

    public static String mUserID;
    //Firebase
    private DatabaseReference mDatabase;
    Date current_date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(current_date);

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
        System.out.println("USERIDIN!!" + this.mUserID);
    }

    public static String getmUserID() {
        return mUserID;
    }

    public void FB_userinfo(String name, String age, String gender, String height, String weight, String disease){
        UserInfo_FB userInfoFB = new UserInfo_FB(name, age, gender,height,weight,disease);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(this.mUserID);
        mDatabase.child("userinfo").setValue(userInfoFB);
    }

    public void FB_exercise(String exercise_name, String time){

        ExerciseInfo_FB exerciseInfoFB =new ExerciseInfo_FB(date,exercise_name,time);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(this.mUserID);
        mDatabase.child("exerciserec").push().setValue(exerciseInfoFB);
    }


}
