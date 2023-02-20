package com.lucky.roadpilot.Models;

public class NoteModel {

    String name,seen,time,title,uid,id,Uname,UImaage,Uphone,Ulat,Ulog,location,dl,key,cc;


    public NoteModel() {
    }

    public NoteModel(String name, String seen, String time, String title, String uid, String id, String uname, String UImaage, String uphone, String ulat, String ulog, String location, String dl, String key, String cc) {
        this.name = name;
        this.seen = seen;
        this.time = time;
        this.title = title;
        this.uid = uid;
        this.id = id;
        Uname = uname;
        this.UImaage = UImaage;
        Uphone = uphone;
        Ulat = ulat;
        Ulog = ulog;
        this.location = location;
        this.dl = dl;
        this.key = key;
        this.cc = cc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUImaage() {
        return UImaage;
    }

    public void setUImaage(String UImaage) {
        this.UImaage = UImaage;
    }

    public String getUphone() {
        return Uphone;
    }

    public void setUphone(String uphone) {
        Uphone = uphone;
    }

    public String getUlat() {
        return Ulat;
    }

    public void setUlat(String ulat) {
        Ulat = ulat;
    }

    public String getUlog() {
        return Ulog;
    }

    public void setUlog(String ulog) {
        Ulog = ulog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
}
