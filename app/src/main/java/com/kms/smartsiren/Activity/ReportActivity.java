package com.kms.smartsiren.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.MapView;
import com.kms.smartsiren.Utility.KNNAlgorithm;
import com.kms.smartsiren.R;
import com.kms.smartsiren.Interface.UnconfirmedCase;
import com.kms.smartsiren.Interface.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportActivity extends AppCompatActivity {
    private KakaoMap kakaoMap;
    MapView mapView;
    private FusedLocationProviderClient fusedLocationClient; //위치 서비스 클라이언트 객체
    private static final int REQUEST_SEND_SMS = 1;
    private Double selectedLatitude = null;
    private Double selectedLongitude = null;
    private RecyclerView recyclerview;
    private Button button4;
    private DatabaseReference mDatabase;
    private String category = null;
    private String report_Title = "";
    private String report_Content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mDatabase = FirebaseDatabase.getInstance().getReference("SmartSiren");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();  // 데이터를 담을 List

        // 각 헤더 아이템과 그에 따른 자식 아이템 생성 및 초기 상태 설정
        ExpandableListAdapter.Item header1 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER1, "");
        header1.invisibleChildren = new ArrayList<>();  // 초기에는 닫혀 있는 상태
        header1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD1, ""));
        data.add(header1);

        ExpandableListAdapter.Item header2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER2, "");
        header2.invisibleChildren = new ArrayList<>();  // 초기에는 닫혀 있는 상태
        header2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD2, ""));
        data.add(header2);

        ExpandableListAdapter.Item header3 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER3, "");
        header3.invisibleChildren = new ArrayList<>();  // 초기에는 닫혀 있는 상태
        header3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD3, ""));
        data.add(header3);

        //recyclerview.addItemDecoration(new RecyclerViewDecoration(30));
        //B
        ExpandableListAdapter adapter = new ExpandableListAdapter(data, new ExpandableListAdapter.OnChildItemClickListener() {
            @Override
            public void onChildItemClick() {
                // CHILD1 아이템 클릭 시 수행할 작업
                Intent intent = new Intent(ReportActivity.this, ReportMapActivity.class);
                reportMapActivityResultLauncher.launch(intent);
            }
            //각 케이스에 따른 버튼
            @Override
            public void onChild2ItemClick(int buttonId) {
                // CHILD2 아이템 내의 버튼 클릭 시 수행할 작업
                switch (buttonId) {
                    case 1:
                        // 버튼1 클릭 시 수행할 작업
                        category = "Fire"; // 화재
                        break;
                    case 2:
                        // 버튼2 클릭 시 수행할 작업
                        category = "Crush"; // 압사
                        break;
                    case 3:
                        // 버튼2 클릭 시 수행할 작업
                        category = "Terror"; // 테러
                        break;
                    case 4:
                        // 버튼2 클릭 시 수행할 작업
                        category = "Collapse"; // 붕괴
                        break;
                    case 5:
                        // 버튼2 클릭 시 수행할 작업
                        category = "Flooding"; // 침수
                        break;
                    case 6:
                        // 버튼2 클릭 시 수행할 작업
                        category = "Other"; // 기타
                        break;
                }
            }
        }, new ExpandableListAdapter.OnChild3DataListener() {
            @Override
            public void onChild3DataChanged(String title, String content) {
                // 여기에서 title과 content를 처리합니다.
                // 예: 전역 변수에 저장
                report_Title = title;
                report_Content = content;
            }
        });
        recyclerview.setAdapter(adapter);


        // Handle button4 click event
        button4 = findViewById(R.id.btn_report);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to 'map' activity
                // 위도와 경도가 선택되었는지 확인
                if(category == null){
                    Toast.makeText(ReportActivity.this, "사건 분류가 설정되지 않았습니다. 사건 분류를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedLatitude != null && selectedLongitude != null) {
                    // Show '신고 접수가 완료되었습니다.' popup
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase.child("UserInfo").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserAccount child = dataSnapshot.getValue(UserAccount.class);
                            UnconfirmedCase A = new UnconfirmedCase(selectedLatitude, selectedLongitude, category,2,report_Title,user.getUid(),String.valueOf(UUID.randomUUID()), child.getReliability());
                            KNNAlgorithm MUC = new KNNAlgorithm();
                            MUC.startLabeling(A, new KNNAlgorithm.MyCallback(){
                                @Override
                                public void onCallback(String value, int depth) {

                                    child.setReportN(child.getReportN() + 1); // 신고 횟수 + 1

                                    // 새로운 사건
                                    if(value.equals("NEW")){
                                        Log.e("사용자", "사건 등록 완료");
                                        mDatabase.child("Unconfirmed").child(A.getUuid()).setValue(A);
                                        child.addReportCase(A.getUuid());
                                    }
                                    // 이미 존재하는 사건
                                    else{
                                        child.addReportCase(value);
                                        if(depth == 0) {
                                            // 신뢰할 수 있는 사건이면 덮어쓰기 되어 아무 작용 안함
                                            child.setReportG(child.getReportG() + 1); // 잘한 신고
                                        }
                                        else{

                                            mDatabase.child("Unconfirmed").child(value).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    UnconfirmedCase caseing = dataSnapshot.getValue(UnconfirmedCase.class);
                                                    caseing.addReport(selectedLatitude, selectedLongitude, user.getUid(), child.getReliability());
                                                    mDatabase.child("Unconfirmed").child(value).setValue(caseing);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Getting Post failed, log a message
                                                    // ...
                                                }
                                            });
                                        }

                                    }
                                    // 사용자 정보 덮어쓰기
                                    mDatabase.child("UserInfo").child(user.getUid()).setValue(child);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // ...
                        }
                    });
                    showReportConfirmationDialog();
                } else {
                    Toast.makeText(ReportActivity.this, "위치가 설정되지 않았습니다. 지도에서 위치를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //B
    private void showReportConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("신고 접수가 완료되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // 액티비티 종료
                    }
                })
                .setNeutralButton("119 신고하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //제목, 내용, 사건케이스 내용 넣어야함
                        String message = String.format(
                                "제목: %s\n위치: 위도 %.6f, 경도 %.6f\n신고 케이스: %s\n내용: %s",
                                report_Title, selectedLatitude, selectedLongitude, category, report_Content);
                        reportTo119(message);
                        finish();
                    }
                })
                .show();
    }
    private void reportTo119(String message) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + 119));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // 뒤로가기 버튼을 눌렀을 때의 동작 설정
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private final ActivityResultLauncher<Intent> reportMapActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    selectedLatitude = data.getDoubleExtra("latitude", 0.0);
                    selectedLongitude = data.getDoubleExtra("longitude", 0.0);
                }
            }
    );
}
