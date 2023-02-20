package com.lucky.roadpilot.Models;

public class JobModel {

    String Vehicle_Data, Driver_data, number, time, Active, uid, Location,No_Wheels,Id;

    public JobModel(String vehicle_Data, String driver_data, String number, String time, String active, String uid, String location, String no_Wheels, String id) {
        Vehicle_Data = vehicle_Data;
        Driver_data = driver_data;
        this.number = number;
        this.time = time;
        Active = active;
        this.uid = uid;
        Location = location;
        No_Wheels = no_Wheels;
        this.Id = id;
    }

    public JobModel() {
    }

    public String getVehicle_Data() {
        return Vehicle_Data;
    }

    public void setVehicle_Data(String vehicle_Data) {
        Vehicle_Data = vehicle_Data;
    }

    public String getDriver_data() {
        return Driver_data;
    }

    public void setDriver_data(String driver_data) {
        Driver_data = driver_data;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNo_Wheels() {
        return No_Wheels;
    }

    public void setNo_Wheels(String no_Wheels) {
        No_Wheels = no_Wheels;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }
}
