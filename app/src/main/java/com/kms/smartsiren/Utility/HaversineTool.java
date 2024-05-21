package com.kms.smartsiren.Utility;

public class HaversineTool {
    // Haversine 공식을 사용하여 두 지점 간의 거리를 계산하는 메서드
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // 지구의 반지름 (미터)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
