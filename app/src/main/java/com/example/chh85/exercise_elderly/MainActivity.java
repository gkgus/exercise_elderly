package com.example.chh85.exercise_elderly;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    UserDB userDB;
    Intent exercise_intent;
    int updated = 3;
    Send_FB fb_userid;
    String user_id;
    ImageView weather_img;
    TextView ex_weather, weather_info, weather_temp, weather_pm;
    Boolean weather_ex_status, pm_ex_status;
    final String TAG = "WEATHER_APP";
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

        final Intent signin_intnet = new Intent(getApplicationContext(), SignInActivity.class);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // 인증이 안 되었다면 인증 화면으로 이동
            startActivity(signin_intnet);
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            user_id = mFirebaseUser.getUid();
            fb_userid.setmUserID(user_id);
            //Check_currentuser();            //이전에 사용했던 유저인지 확인한다.
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        //Button user_info_btn = (Button) findViewById(R.id.user_info);
        //Button exercise_btn = (Button) findViewById(R.id.exercise_btn);
        //Button record_btn = (Button) findViewById(R.id.record_btn);
        LinearLayout ex_layout = (LinearLayout) findViewById(R.id.ex_layout);
        final ImageButton userinfo_btn, exercise_btn, record_btn, healthinfo_btn;
        userinfo_btn = (ImageButton) findViewById(R.id.userinfo_btn);
        exercise_btn = (ImageButton) findViewById(R.id.exercise_btn);
        record_btn = (ImageButton) findViewById(R.id.record_btn);
        healthinfo_btn = (ImageButton) findViewById(R.id.healthinfo_btn);
        weather_img = (ImageView) findViewById(R.id.weather_icon);
        ex_weather=(TextView) findViewById(R.id.ex_weather);
        weather_info=(TextView)findViewById(R.id.weather_info);
        weather_temp=(TextView)findViewById(R.id.weather_temp);
        weather_pm=(TextView)findViewById(R.id.weather_pm);
        userDB = new UserDB(this, "user_db", null, 1);


        String apiURL="http://api.openweathermap.org/data/2.5/weather?lat=37.318199&lon=127.087898&units=metric&appid=19f354e638b73566d4d7c17c303213fd";
        ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
        receiveUseTask.execute(apiURL);
        getPM();


        userinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_info_intent = new Intent(getApplicationContext(), userinfo_act.class);
                startActivityForResult(user_info_intent, 0);
            }

        });

        exercise_intent = new Intent(getApplicationContext(), Exercise_act.class);

        exercise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDB.exist_db(userDB) == true) {  //DB에 정보가 있으면 보여준다. 없으면 운동을 실행할 수 없음.
                    startActivity(exercise_intent);
                } else {
                    Toast.makeText(getApplicationContext(), "내 정보를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        System.out.println("=====================" + userDB.exist_db(userDB));

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record_intent = new Intent(getApplicationContext(), Record_act.class);
                startActivity(record_intent);
            }
        });

        recordDB = new RecordDB(this, "record_exercise", null, 1);
        if (recordDB.exist_db(recordDB) == true) {
            System.out.println("recordDB existed!");
        } else {
            System.out.println("recordDB no existed!");
            recordDB.backupDB(recordDB);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    getFB();
                }
            });
            th.run();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        exercise_intent.putExtra("updated", updated);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:     //not updated
                updated = 0;
                break;
            case 1:     //updated
                updated = 1;
                break;
        }
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
//                        String result = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                        return jObject;
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(JSONObject result) {
            Log.i(TAG, result.toString());
            if (result != null) {

                String iconName = "";
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";

                String humidity = "";
                String speed = "";
                String main = "";
                String description = "";

                try {
                    iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").getString("temp");
                    humidity = result.getJSONObject("main").getString("humidity");
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    speed = result.getJSONObject("wind").getString("speed");
                    main = result.getJSONArray("weather").getJSONObject(0).getString("main");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String msg = description + " 습도 " + humidity + "%, 풍속 " + speed + "m/s" + " 온도 현재:" + nowTemp + " / 최저:" + minTemp + " / 최고:" + maxTemp;
                System.out.println("==========================" + msg);
                System.out.println("==========================" + description);
                String img_url = "http://openweathermap.org/img/w/" + iconName + ".png";
                Picasso.get().load(img_url).into(weather_img);
                String weather_description = weather_icon_description(iconName);
                weather_info.setText(weather_description);
                weather_temp.setText("현재 기온: "+nowTemp);
                if(Double.parseDouble(nowTemp)>=35.0 || Double.parseDouble(nowTemp)<=-5.0){
                    weather_ex_status=false;
                }
                String ex_weather_status = exercise_weather();
                ex_weather.setText(ex_weather_status);
                //weather_img.setImageResource();
            }

        }


        //이미지를 바탕으로 날씨 표시
        private String weather_icon_description(String weatherIcon){
            String weather_description ="";
            if(weatherIcon.contains("01")==true){
                weather_description="맑음";
                weather_ex_status=true;
            }
            if(weatherIcon.contains("02")==true){
                weather_description="구름 조금";
                weather_ex_status=true;
            }
            if(weatherIcon.contains("03")==true){
                weather_description="구름 많음";
                weather_ex_status=true;
            }
            if(weatherIcon.contains("04")==true){
                weather_description="흐림";
                weather_ex_status=true;
            }
            if(weatherIcon.contains("09")==true){
                weather_description="소나기";
                weather_ex_status=false;
            }
            if(weatherIcon.contains("10")==true){
                weather_description="비";
                weather_ex_status=false;
            }
            if(weatherIcon.contains("11")==true){
                weather_description="천둥 번개";
                weather_ex_status=false;
            }
            if(weatherIcon.contains("13")==true){
                weather_description="눈";
                weather_ex_status=false;
            }
            if(weatherIcon.contains("50")==true){
                weather_description="안개";
                weather_ex_status=true;
            }

            return weather_description;

        }

    }

    public String exercise_weather(){
        String good_weather ="운동하기 좋은 날씨";
        String bad_weather = "운동을 자제해 주세요";

        if(weather_ex_status&&pm_ex_status){
            return good_weather;
        } else {
            return bad_weather;
        }

    }
        public void Check_currentuser() {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //for(DataSnapshot data: dataSnapshot.getChildren()){ }
                    if (dataSnapshot.hasChild(user_id)) {       //사용자 아이디에 정보가 존재한다.
                        System.out.println("=====USERID EXISTS!!====" + user_id);

                    } else {
                        System.out.println("=====NO USERID====" + user_id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    public void getPM(){

        boolean initem=false, dataTime = false, seoul_pm = false, gyeonggi_pm = false;
        StrictMode.enableDefaults();
        int pm_rate=0;
        String dataTime_str = null, seoul_pm_str = null, gyeonggi_pm_str = null;

        try{
            URL url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?" +
                    "serviceKey=DZl7LZ1uLqXNmWKBp7qmlVmiMNgKS2B32rAmciES1NRS8xKIt%2F8%2BDT6gtsO45FcxheJYbse0BhIKkQp%2BB4B4cQ%3D%3D&numOfRows=10&pageSize=10&pageNo=1&startPage=1&itemCode=PM10&dataGubun=HOUR&searchCondition=MONTH"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");
//parserEvent != XmlPullParser.END_DOCUMENT
            while (initem==false){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("dataTime")){ //dataTime 만나면 내용을 받을수 있게 하자
                            dataTime = true;
                        }
                        if(parser.getName().equals("seoul")){ //seoul 만나면 내용을 받을수 있게 하자
                            seoul_pm = true;
                        }
                        if(parser.getName().equals("gyeonggi")){ //gyeonggi 만나면 내용을 받을수 있게 하자
                            gyeonggi_pm = true;
                        }

                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            weather_pm.setText(weather_pm.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(dataTime){ //isTitle이 true일 때 태그의 내용을 저장.
                            dataTime_str = parser.getText();
                            dataTime = false;
                        }
                        if(seoul_pm){ //isAddress이 true일 때 태그의 내용을 저장.
                            seoul_pm_str = parser.getText();
                            seoul_pm = false;
                        }
                        if(gyeonggi_pm){ //isMapx이 true일 때 태그의 내용을 저장.
                            gyeonggi_pm_str = parser.getText();
                            gyeonggi_pm = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            pm_rate=Integer.parseInt(gyeonggi_pm_str);
                           System.out.println("시간 : "+ dataTime_str +"\n 서울: "+ seoul_pm_str +"\n 경기 : " + gyeonggi_pm_str+"\n");
                            initem = true;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            weather_pm.setText("에러가..났습니다...");
        }

        if(pm_rate<16){
            weather_pm.setText("미세먼지: 좋음");
            pm_ex_status=true;
        } else if(16<=pm_rate&&pm_rate<36){
            weather_pm.setText("미세먼지: 보통");
            pm_ex_status=true;

        } else if(pm_rate>=36){
            weather_pm.setText("미세먼지: 나쁨");
            pm_ex_status=false;
        }


    }

        public void getFB() {
            mRead_FB = FirebaseDatabase.getInstance().getReference().child(user_id).child("exerciserec");
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
                        recordDB.insertDB(recordDB, ex_list.get(0), ex_list.get(1), ex_list.get(2));
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

