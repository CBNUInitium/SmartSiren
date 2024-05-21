package com.kms.smartsiren.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kms.smartsiren.Utility.KNNAlgorithm;
import com.kms.smartsiren.R;
import com.kms.smartsiren.Interface.UnconfirmedCase;

public class TestActivity extends AppCompatActivity {

    TextView tv_lat, tv_long, tv_name;
    Button btn_insert, btn_output;
    EditText et_rat, et_long;
    private DatabaseReference mDatabaseRef; // 실시간 DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        et_long = findViewById(R.id.et_long);
        et_rat = findViewById(R.id.et_rat);
        tv_lat = findViewById(R.id.tv_result_lat);
        tv_long = findViewById(R.id.tv_result_long);
        tv_name = findViewById(R.id.tv_result_name);
        btn_insert = findViewById(R.id.btn_insert);
        btn_output = findViewById(R.id.btn_output);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SmartSiren");

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double l = Double.parseDouble(et_long.getText().toString().trim());
                double r = Double.parseDouble(et_rat.getText().toString().trim());
                //CaseInfo A = new CaseInfo(l, r, "...", 2, "자세히", String.valueOf(UUID.randomUUID()));
                //mDatabaseRef.child("Unconfirmed").child("12345").setValue(A);
                UnconfirmedCase a = new UnconfirmedCase(l, r, "1",2,"안녕","abc","avdcdcas",13);
                mDatabaseRef.child("Unconfirmed").child(a.getUuid()).setValue(a);
            }
        });
        btn_output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double l = Double.parseDouble(et_long.getText().toString().trim());
                double r = Double.parseDouble(et_rat.getText().toString().trim());
                UnconfirmedCase a = new UnconfirmedCase(l, r, "1",2,"안녕","abc","avdcdcas",10);
                KNNAlgorithm MUC = new KNNAlgorithm();

                mDatabaseRef.child("Unconfirmed").child("avdcdcas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UnconfirmedCase caseing = dataSnapshot.getValue(UnconfirmedCase.class);
                        caseing.setReliability(caseing.getReliability() + 1);
                        mDatabaseRef.child("Unconfirmed").child(caseing.getUuid()).setValue(caseing);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        // ...
                    }
                });

                /*
                MUC.startLabeling(a, new KNNAlgorithm.MyCallback(){
                    @Override
                    public void onCallback(String value, int depth) {
                        tv_name.setText(value);
                    }
                });*/
            }
        });
    }
}