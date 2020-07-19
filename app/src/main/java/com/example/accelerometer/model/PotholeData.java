package com.example.accelerometer.model;

public class PotholeData {
    double x_axis,y_axis,z_axis,latitude,longitude;
    int potholeExist;
    int sr_no;

    public PotholeData() {
        this.x_axis = 0.0;
        this.y_axis = 0.0;
        this.z_axis = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.potholeExist = 0;
        this.sr_no = 0;
    }



    public double getX_axis() {
        return x_axis;
    }

    public void setX_axis(double x_axis) {
        this.x_axis = x_axis;
    }

    public double getY_axis() {
        return y_axis;
    }

    public void setY_axis(double y_axis) {
        this.y_axis = y_axis;
    }

    public double getZ_axis() {
        return z_axis;
    }

    public void setZ_axis(double z_axis) {
        this.z_axis = z_axis;
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

    public int getPotholeExist() {
        return potholeExist;
    }

    public void setPotholeExist(int potholeExist) {
        this.potholeExist = potholeExist;
    }

    public int getSr_no() {
        return sr_no;
    }

    public void setSr_no(int sr_no) {
        this.sr_no = sr_no;
    }

    @Override
    public String toString() {
        return "PotholeData{" +
                "x_axis=" + x_axis +
                ", y_axis=" + y_axis +
                ", z_axis=" + z_axis +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", potholeExist=" + potholeExist +
                ", sr_no=" + sr_no +
                '}';
    }
}
