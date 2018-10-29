package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    UserDB userDB;
    Intent exercise_intent;
    int updated =3;
    Send_FB fb_userid;

    //firebase login activity
    private static final int REQUEST_INVITE = 1000;

    // Firebase 인스턴스 변수
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    // Google
    private GoogleApiClient mGoogleApiClient;
    // 사용자 이름과 사진
    private String mUsername;
    private String mPhotoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fb_userid = new Send_FB();

        final Intent signin_intnet = new Intent(getApplicationContext(),SignInActivity.class);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // 인증이 안 되었다면 인증 화면으로 이동
            startActivity(signin_intnet);
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            fb_userid.setmUserID(mFirebaseUser.getUid());
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        //Button user_info_btn = (Button) findViewById(R.id.user_info);
        Button exercise_btn = (Button) findViewById(R.id.exercise_btn);
        Button record_btn = (Button) findViewById(R.id.record_btn);
        final ImageButton userinfo_btn;
        userinfo_btn = (ImageButton) findViewById(R.id.userinfo_btn);
        userDB = new UserDB(this,"user_db",null,1);


        userinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_info_intent = new Intent(getApplicationContext(),userinfo_act.class);
                startActivityForResult(user_info_intent,0);
            }

        });

        exercise_intent = new Intent(getApplicationContext(),Exercise_act.class);

        exercise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userDB.exist_db(userDB)==true){
                    startActivity(exercise_intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"내 정보를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
            }
        });

        System.out.println("====================="+userDB.exist_db(userDB));

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_intent = new Intent(getApplicationContext(),Record_act.class);
                startActivity(record_intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

            exercise_intent.putExtra("updated",updated);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 0:     //not updated
                updated =0;
                break;
            case 1:     //updated
                updated = 1;
                break;
        }
    }
}
