package com.kms.smartsiren.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kms.smartsiren.R;

public class UserEditActivity extends AppCompatActivity {

    Button btnuseredit;
    EditText et_email, et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        et_email = findViewById(R.id.et_DBemail);
        et_email.setText(user.getEmail());

        et_pwd = findViewById(R.id.et_pw_chk);


        btnuseredit = findViewById(R.id.btn_useredit);

        btnuseredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();

                if(pwd.equals("")){
                    Toast.makeText(UserEditActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.equals("")){
                    Toast.makeText(UserEditActivity.this, "예상치 못한 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(email, pwd);

                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(UserEditActivity.this, UserEdit_2_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(UserEditActivity.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                            }
                        });


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