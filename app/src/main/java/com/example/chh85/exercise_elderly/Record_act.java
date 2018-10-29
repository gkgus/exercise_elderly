package com.example.chh85.exercise_elderly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Record_act extends AppCompatActivity {
    private DatabaseReference mRead_FB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_act);
        String mUserID = Send_FB.getmUserID();
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
