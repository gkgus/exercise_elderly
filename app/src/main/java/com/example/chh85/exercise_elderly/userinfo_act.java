package com.example.chh85.exercise_elderly;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;


public class userinfo_act extends AppCompatActivity {

    UserDB userdb;
    SQLiteDatabase sqlDB;
    EditText name_et, age_et, height_et,weight_et;
    RadioGroup gender_et;
    CheckBox high_bp,guanjeol,diabetes,gojihyeol;
    RadioButton male, female;

    Send_FB send_userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("내 정보");
        setContentView(R.layout.activity_userinfo_act);

        name_et = (EditText) findViewById(R.id.name);
        age_et = (EditText) findViewById(R.id.age);
        height_et = (EditText) findViewById(R.id.height);
        weight_et = (EditText) findViewById(R.id.weight);
        gender_et = (RadioGroup) findViewById(R.id.gender_group);
        high_bp = (CheckBox) findViewById(R.id.high_bp);
        guanjeol = (CheckBox) findViewById(R.id.guanjeol);
        diabetes = (CheckBox) findViewById(R.id.diabetes);
        gojihyeol = (CheckBox) findViewById(R.id.goji);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);



        userdb = new UserDB(this,"user_db",null,1);
        if(userdb.exist_db(userdb)==true){
            ArrayList<String> test_list;
            test_list=userdb.readDB(userdb);
            name_et.setText(test_list.get(0));
            age_et.setText(test_list.get(1));
            System.out.println("---------------------"+test_list.get(2));
            if(test_list.get(2).equals("m")){
               gender_et.check(R.id.male);
            }
            if(test_list.get(2).equals("f")){
                gender_et.check(R.id.female);
            }
            height_et.setText(test_list.get(3));
            weight_et.setText(test_list.get(4));
            if(test_list.get(5).contains("h")){
                high_bp.setChecked(true);
            }
            if(test_list.get(5).contains("g")){
                guanjeol.setChecked(true);
            }
            if(test_list.get(5).contains("d")){
                diabetes.setChecked(true);
            }
            if(test_list.get(5).contains("j")){
                gojihyeol.setChecked(true);
            }


        }
        System.out.println("존재하냐!!!!!!!============"+userdb.exist_db(userdb));
        Button save_btn = (Button) findViewById(R.id.save_btn);
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn);



        //취소 버튼
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });

        final String name,age,gender,height,weight,disease;
        name = name_et.getText().toString();
        age=age_et.getText().toString();
        gender=ischecked.RG_checked(gender_et);
        height=height_et.getText().toString();
        weight=weight_et.getText().toString();
        disease=ischecked.CB_checked(high_bp,guanjeol,diabetes,gojihyeol);
        send_userinfo = new Send_FB();
        //저장버튼
        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //DB저장
                sqlDB = userdb.getWritableDatabase();
                userdb.onUpgrade(sqlDB,1,2);
                sqlDB.execSQL("INSERT INTO user_info VALUES ( '" +name_et.getText().toString() +"','"
                +age_et.getText().toString()+"','"
                +ischecked.RG_checked(gender_et)+"','"
                +height_et.getText().toString()+"','"
                +weight_et.getText().toString()+"','"
                +ischecked.CB_checked(high_bp,guanjeol,diabetes,gojihyeol)+"');");
                sqlDB.close();

                send_userinfo.FB_userinfo(name,age,gender,height,weight,disease);

                setResult(1);
                finish();

            }
        });

    }

    public class db_null_exception extends RuntimeException {

    }

}
