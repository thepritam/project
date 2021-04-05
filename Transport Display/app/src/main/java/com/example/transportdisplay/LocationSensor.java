package com.example.transportdisplay;

public class LocationSensor {
    public double latitude;
    public double longitude;
//    public double accuracy;
//    public double altitude;
//    public double bearing;
//    public double bearingAccuracyDegrees;
//    public boolean complete;
//    public long elapsedRealtimeNanos;
//    public boolean fromMockProvider;
//    public String provider;
//    public long speed;
//    public long speedAccuracyMetersPerSecond;
//    public long time;
//    public long verticalAccuracyMeters;

    public LocationSensor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

//    public LocationSensor(double accuracy, double altitude,double bearing,double bearingAccuracyDegrees,boolean complete,long elapsedRealtimeNanos,boolean fromMockProvider,double latitude, double longitude,String provider,long speed,long speedAccuracyMetersPerSecond,long time,long verticalAccuracyMeters) {


            public LocationSensor(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
//        this.accuracy = accuracy;
//        this.altitude = altitude;
//        this.bearing = bearing;
//        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
//        this.complete = complete;
//        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
//        this.fromMockProvider = fromMockProvider;
//        this.provider = provider;
//        this.speed = speed;
//        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
//        this.time = time;
//        this.verticalAccuracyMeters = verticalAccuracyMeters;

    }

}
