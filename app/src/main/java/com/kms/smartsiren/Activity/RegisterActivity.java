package com.kms.smartsiren.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kms.smartsiren.R;
import com.kms.smartsiren.Utility.ReliabilityModel;
import com.kms.smartsiren.Interface.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // DB 인증 관련
    private DatabaseReference mDatabaseRef; // 실시간 DB

    EditText et_name, et_email, et_pwd, et_pwd_chk;
    Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SmartSiren");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_name = findViewById(R.id.et_name);
        et_pwd_chk = findViewById(R.id.et_pwd_chk);
        btn_register = findViewById(R.id.btn_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String pwd_chk = et_pwd_chk.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    // 아이디, 비밀번호, 이름 중 하나라도 비어있을 경우 경고 메시지 표시
                    Toast.makeText(RegisterActivity.this, "이름, 이메일, 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 8) {
                    // 비밀번호가 8자 이하일 경우 팝업 메시지 표시
                    Toast.makeText(RegisterActivity.this, "비밀번호를 8자 이상 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    // Check if email format is valid
                    Toast.makeText(RegisterActivity.this, "올바른 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (!pwd.equals(pwd_chk)) {
                    // Check if passwords match
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다. 동일한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    et_pwd.setText(""); // Clear the password fields
                    et_pwd_chk.setText("");
                } else {
                    // 모든 필드가 입력되고 비밀번호가 8자 이상일 경우 회원가입 성공 팝업을 띄우고 로그인 화면으로 이동
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                UserAccount account = new UserAccount(email,pwd,name, ReliabilityModel.USER_RELIABILITY_DEFAULT,0,0);

                                mDatabaseRef.child("UserInfo").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish(); // 현재 화면을 종료하여 뒤로 가기 시에 다시 돌아오지 않도록 함
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
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

    private boolean isValidEmail(String email) {
        // 정규식을 사용하여 이메일 형식을 검증
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

}