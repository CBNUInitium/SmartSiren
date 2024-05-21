package com.kms.smartsiren.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kms.smartsiren.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // DB 인증 관련
    private DatabaseReference mDatabaseRef; // 실시간 DB

    Button btn_login;
    Button btn_signup;
    EditText et_email; // 아이디 입력란
    EditText et_pwd; // 비밀번호 입력란

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SmartSiren");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mDatabaseRef.child("UserInfo").child(mFirebaseAuth.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                                    //데이터 가져오기
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.getValue(String.class);
                                        Toast.makeText(LoginActivity.this, value, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                                    }
                                });
                                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                                startActivity(intent);
                                finish();

                                //getSubActivityResult.launch(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}