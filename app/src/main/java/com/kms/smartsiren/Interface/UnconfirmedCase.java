package com.kms.smartsiren.Interface;

import com.kms.smartsiren.Utility.ReliabilityModel;

import java.util.ArrayList;

public class UnconfirmedCase extends CaseInfo{
    private int personnel; //인원 수
    ArrayList<String> informants;

    private int reliability; // 신용도

    public UnconfirmedCase(){}

    public UnconfirmedCase(double latitude, double longitude, String category, int rating, String detail, String informant, String uuid, int reliability) {
        super(latitude, longitude, category, rating, detail, uuid);
        this.informants = new ArrayList<>();
        this.informants.add(informant);
        this.personnel = this.informants.size();
        this.reliability = reliability;
    }

    public void addReport(double latitude, double longitude, String informant, int real){
        setLatitude( (getLatitude() * personnel + latitude) / (personnel + 1) );
        setLongitude( (getLongitude() * personnel + longitude) / (personnel + 1));
        personnel += 1;
        informants.add(informant);
        setReliability(ReliabilityModel.getCaseReliability(getReliability(), real, personnel));
    }

    public int getReliability() {
        return reliability;
    }

    public void setReliability(int reliability) {
        this.reliability = reliability;
    }

    public int getPersonnel() {
        return personnel;
    }

    public void setPersonnel(int personnel) {
        this.personnel = personnel;
    }

    public ArrayList<String> getInformants() {
        return informants;
    }

    public void setInformants(ArrayList<String> informants) {
        this.informants = informants;
    }
}
