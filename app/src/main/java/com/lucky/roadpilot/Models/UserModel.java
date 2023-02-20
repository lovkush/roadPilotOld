package com.lucky.roadpilot.Models;

public class UserModel {

    String Cat,name,phone,Image,Location,uid,Company_Name,DrivingF_Image,FPan,Pan_Image;
    double  lon,lat;

    public UserModel() {
    }


    public UserModel(String cat, String name, String phone, String image, String location, String uid, String company_Name, String drivingF_Image, String FPan, String pan_Image, double lon, double lat) {
        Cat = cat;
        this.name = name;
        this.phone = phone;
        Image = image;
        Location = location;
        this.uid = uid;
        Company_Name = company_Name;
        DrivingF_Image = drivingF_Image;
        this.FPan = FPan;
        Pan_Image = pan_Image;
        this.lon = lon;
        this.lat = lat;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDrivingF_Image() {
        return DrivingF_Image;
    }

    public void setDrivingF_Image(String drivingF_Image) {
        DrivingF_Image = drivingF_Image;
    }

    public String getFPan() {
        return FPan;
    }

    public void setFPan(String FPan) {
        this.FPan = FPan;
    }

    public String getPan_Image() {
        return Pan_Image;
    }

    public void setPan_Image(String pan_Image) {
        Pan_Image = pan_Image;
    }
}
