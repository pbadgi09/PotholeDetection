package com.example.accelerometer.model;

public class PotholeLatLong {
    double latitude;
    double longitude;

    public PotholeLatLong() {
    }

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

    public PotholeLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "PotholeLatLong{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

