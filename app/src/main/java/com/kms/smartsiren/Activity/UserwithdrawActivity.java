package com.kms.smartsiren.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kms.smartsiren.R;

public class UserwithdrawActivity extends AppCompatActivity {

    Button btn_cancel, btn_userwithdraw, btn_agree;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userwithdraw);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_userwithdraw = findViewById(R.id.btn_userwithdraw);
        btn_agree = findViewById(R.id.btn_agree);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // DB에서 회원 정보 삭제 기능
        btn_userwithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_agree.isSelected()) {
                    // btn_agree가 true인 경우에만 작동하도록 설정
                    mFirebaseAuth.getCurrentUser().delete();
                    Intent intent = new Intent(UserwithdrawActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // btn_agree가 false인 경우 안내 Toast 메시지 표시
                    Toast.makeText(UserwithdrawActivity.this, "안내를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_agree.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = false; // 버튼 상태를 추적하는 변수

            @Override
            public void onClick(View v) {
                isSelected = !isSelected; // 상태를 토글

                // 버튼 상태에 따라 색상 변경
                if (isSelected) {
                    // 버튼이 선택된 경우 색상 변경
                    btn_agree.setSelected(true); // 선택된 경우 색상 변경 또는 배경 변경
                } else {
                    // 버튼이 선택되지 않은 경우 색상 변경
                    btn_agree.setSelected(false); // 선택되지 않은 경우 색상 변경 또는 배경 변경
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
}