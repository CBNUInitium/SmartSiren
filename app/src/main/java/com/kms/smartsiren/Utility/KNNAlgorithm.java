package com.kms.smartsiren.Utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kms.smartsiren.Interface.CaseInfo;
import com.kms.smartsiren.Interface.UnconfirmedCase;

import java.util.ArrayList;
import java.util.List;


public class KNNAlgorithm {
    private List<CaseInfo> trainingData = new ArrayList<CaseInfo>();
    public CaseInfo chk;
    public String predict_label = "NEW";
    private static final double RADIUS_THRESHOLD = 50; // 1미터

    public interface MyCallback {
        void onCallback(String value, int depth);
    }

    public KNNAlgorithm(List<CaseInfo> trainingData) {
        this.trainingData = trainingData;
    }
    public KNNAlgorithm() {}

    public void addTrainingData(CaseInfo e){
        trainingData.add(e);
    }

    public void clearTrainingData(){
        if( trainingData != null && !trainingData.isEmpty()) {
            trainingData.clear();
        }
    }

    // k-NN 알고리즘 예측 메서드 (k = 1)
    public String predict(CaseInfo testPoint) {
        Log.e("pre", "Start Predict");
        if (trainingData == null || trainingData.isEmpty()) {
            Log.e("pre", "EMty");
            // 훈련 데이터가 비어있을 때
            return "NEW";
        }

        // 테스트 데이터와의 거리 계산 후 정렬
        List<CaseInfo> sortedData = new ArrayList<>(trainingData);
        sortedData.sort((dp1, dp2) -> Double.compare(dp1.calculateManhattanDistance(testPoint),
                dp2.calculateManhattanDistance(testPoint)));

        // 가장 가까운 이웃 선택 (k = 1)
        CaseInfo nearestNeighbor = sortedData.get(0);
        Log.e("pre", "choose one " + nearestNeighbor.calculateManhattanDistance(testPoint));
        // 거리가 r = 0.5 이상이면 UUID 발급
        if (nearestNeighbor.calculateManhattanDistance(testPoint) >= RADIUS_THRESHOLD) {
            Log.e("pre", "far");
            return "NEW";
        }
        Log.e("pre", "near");
        // 그 외의 경우 가장 가까운 이웃의 레이블 반환
        return (nearestNeighbor.getUuid());
    }
    public void readData_Un(MyCallback myCallback){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("SmartSiren");

        Query mergeUnconQuery = databaseReference.child("Unconfirmed");

        mergeUnconQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CaseInfo child = postSnapshot.getValue(UnconfirmedCase.class);
                    addTrainingData(child);
                }
                predict_label = predict(chk);
                clearTrainingData();
                myCallback.onCallback(predict_label, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readData(MyCallback myCallback){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("SmartSiren");

        Query mergeCaseQuery = databaseReference.child("CaseInfo");


        mergeCaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CaseInfo child = postSnapshot.getValue(CaseInfo.class);
                    addTrainingData(child);
                }
                predict_label = predict(chk);
                clearTrainingData();
                myCallback.onCallback(predict_label, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });
    }

    public void startLabeling(CaseInfo pivot, MyCallback outCallback){
        chk = pivot;
        Log.e("KNN", "Start Labeling");
        readData(new MyCallback() {
            @Override
            public void onCallback(String value, int depth) {
                if(value.equals("NEW")){
                    readData_Un(new MyCallback() {
                        @Override
                        public void onCallback(String value, int depth) {
                            outCallback.onCallback(value, 1);
                        }
                    });
                }
                else{
                    outCallback.onCallback(value, 0);
                }
            }
        });
    }
}
