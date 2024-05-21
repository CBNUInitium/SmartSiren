package com.kms.smartsiren.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kms.smartsiren.Interface.CaseInfo;
import com.kms.smartsiren.R;
import com.kms.smartsiren.Interface.UnconfirmedCase;
import com.kms.smartsiren.Interface.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyupdateActivity extends AppCompatActivity {

    private Map<String, String> wordMap = new HashMap<>();
    public interface MyCallback {
        void onCallback(String value, String value2);
    }
    TextView tv_name, tv_db1, tv_db2, tv_db3, tv_db4, text_DBnumber, tv_table;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupdate);

        wordMap.put("Fire", "화재");
        wordMap.put("Crush", "압사");
        wordMap.put("Terror", "테러");
        wordMap.put("Collapse", "붕괴");
        wordMap.put("Flooding", "침수");
        wordMap.put("Other", "기타");

        text_DBnumber = findViewById(R.id.text_DBnumber);
        tv_table = findViewById(R.id.tv_table);

        tv_name = findViewById(R.id.text_DBname2);
        tv_db1 = findViewById(R.id.text_DB1); //신고접수
        tv_db2 = findViewById(R.id.text_DB2); //반려
        tv_db3 = findViewById(R.id.text_DB3); //처리중
        tv_db4 = findViewById(R.id.text_DB4); //처리완료

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SmartSiren");
        mDatabaseRef.child("UserInfo").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                tv_name.setText(userAccount.getName());
                int n = userAccount.getReportN();
                int g = userAccount.getReportG();
                tv_db1.setText(Integer.toString(n));
                tv_db2.setText("0");
                tv_db3.setText(Integer.toString(n - g));
                tv_db4.setText(Integer.toString(g));
                ArrayList<String> list = userAccount.getReportCase();
                if(list == null){
                    text_DBnumber.setText(0);
                    return;
                }
                text_DBnumber.setText(Integer.toString(list.size()));
                for (String element:list) {
                    getCaseName(element, new MyCallback() {

                        @Override
                        public void onCallback(String value, String value2) {
                            tv_table.setText(tv_table.getText().toString().trim() + "\n[" + wordMap.get(value) + "] " + value2);
                        }
                    });

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void getCaseName(String id, MyCallback myCallback){
        mDatabaseRef.child("CaseInfo").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot == null){
                    mDatabaseRef.child("CaseInfo").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot == null){
                                return;
                            }
                            UnconfirmedCase unconfirmedCase = snapshot.getValue(UnconfirmedCase.class);
                            myCallback.onCallback(unconfirmedCase.getCategory(),unconfirmedCase.getDetail());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    CaseInfo caseInfo = snapshot.getValue(CaseInfo.class);
                    myCallback.onCallback(caseInfo.getCategory(),caseInfo.getDetail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // 뒤로가기 버튼을 눌렀을 때의 동작
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
