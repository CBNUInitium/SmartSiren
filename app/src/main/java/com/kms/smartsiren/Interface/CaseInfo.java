package com.kms.smartsiren.Interface;

import com.kms.smartsiren.Utility.HaversineTool;

public class CaseInfo {
    private double latitude; // 위도
    private double longitude; // 경도
    private String category; //사건 분류
    private int rating; //위험 등급
    private String detail; // 사건 정보
    private String uuid;

    public CaseInfo(){}

    public CaseInfo(double latitude, double longitude, String category, int rating, String detail, String uuid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.rating = rating;
        this.detail = detail;
        this.uuid = uuid;
    }
    public double calculateManhattanDistance(CaseInfo otherPoint) {
        return HaversineTool.haversine(this.latitude, this.longitude, otherPoint.latitude, otherPoint.longitude);
    }

    /*
    public int getDistance(){
        int dis = 0;
        switch (category){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                break;
        }
        return dis;
    }*/

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
