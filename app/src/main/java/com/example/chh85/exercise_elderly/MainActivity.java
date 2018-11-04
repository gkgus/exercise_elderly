package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    UserDB userDB;
    Intent exercise_intent;
    int updated =3;
    Send_FB fb_userid;
    String user_id;
    //firebase login activity
    private static final int REQUEST_INVITE = 1000;

    // Firebase 인스턴스 변수
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    // Google
    private GoogleApiClient mGoogleApiClient;
    // 사용자 이름과 사진
    private String mUsername;
    private String mPhotoUrl;

    private DatabaseReference mRead_FB;
    RecordDB recordDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();
        ab.setTitle("60+ 운동");
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
            user_id=mFirebaseUser.getUid();
            fb_userid.setmUserID(user_id);
            //Check_currentuser();            //이전에 사용했던 유저인지 확인한다.
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        //Button user_info_btn = (Button) findViewById(R.id.user_info);
        //Button exercise_btn = (Button) findViewById(R.id.exercise_btn);
        //Button record_btn = (Button) findViewById(R.id.record_btn);
        final ImageButton userinfo_btn,exercise_btn,record_btn,healthinfo_btn;
        userinfo_btn = (ImageButton) findViewById(R.id.userinfo_btn);
        exercise_btn = (ImageButton) findViewById(R.id.exercise_btn);
        record_btn = (ImageButton) findViewById(R.id.record_btn);
        healthinfo_btn=(ImageButton) findViewById(R.id.healthinfo_btn);
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
                if(userDB.exist_db(userDB)==true){  //DB에 정보가 있으면 보여준다. 없으면 운동을 실행할 수 없음.
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

        recordDB = new RecordDB(this,"record_exercise",null,1);
        recordDB.existDB(recordDB);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                getFB();
            }
        });
        th.run();
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

    public void Check_currentuser(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for(DataSnapshot data: dataSnapshot.getChildren()){ }
                if (dataSnapshot.hasChild(user_id)) {       //사용자 아이디에 정보가 존재한다.
                    System.out.println("=====USERID EXISTS!!===="+user_id);

                } else {
                    System.out.println("=====NO USERID===="+user_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void getFB() {
        mRead_FB= FirebaseDatabase.getInstance().getReference().child(user_id).child("exerciserec");
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
                    //System.out.println("====="+ex_list.get(0)+" "+ex_list.get(1)+" "+ex_list.get(2));
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

}
