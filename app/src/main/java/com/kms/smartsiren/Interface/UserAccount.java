package com.kms.smartsiren.Interface;

import java.util.ArrayList;

public class UserAccount {
    private String emailID;
    private String password;
    private String name;
    private int reliability;
    private int reportN;
    private int reportG;
    private double latitudeLast;
    private double longitudeLast;
    private ArrayList<String> reportCase = new ArrayList<String>();

    public UserAccount(String emailID, String password, String name, int reliability, int reportN, int reportG) {
        this.emailID = emailID;
        this.password = password;
        this.name = name;
        this.reliability = reliability;
        this.reportN = reportN;
        this.reportG = reportG;
        this.latitudeLast = 0;
        this.longitudeLast = 0;
    }

    public int getReportG() {
        return reportG;
    }

    public void setReportG(int reportG) {
        this.reportG = reportG;
    }


    public int getReportN() {
        return reportN;
    }

    public void setReportN(int reportN) {
        this.reportN = reportN;
    }

    public double getLatitudeLast() {
        return latitudeLast;
    }

    public void setLatitudeLast(double latitudeLast) {
        this.latitudeLast = latitudeLast;
    }

    public double getLongitudeLast() {
        return longitudeLast;
    }

    public void setLongitudeLast(double longitudeLast) {
        this.longitudeLast = longitudeLast;
    }

    public int getReliability() {
        return reliability;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getReportCase() {
        return reportCase;
    }

    public void setReportCase(ArrayList<String> reportCase) {
        this.reportCase = reportCase;
    }

    public void addReportCase(String reportId){
        if(this.reportCase == null){
            this.reportCase = new ArrayList<String>();
        }
        this.reportCase.add(reportId);
    }

    public UserAccount() { } //빈 생성자 꼭 생성해야 DB접근 가능


}
