package com.lucky.roadpilot.Models;

public class PostModel {

    String Cat,post_img,uid,time,approved,title,Description,Location;
    String Id;

    public PostModel(String cat, String post_img, String uid, String time, String approved, String title, String description, String location, String id) {
        Cat = cat;
        this.post_img = post_img;
        this.uid = uid;
        this.time = time;
        this.approved = approved;
        this.title = title;
        Description = description;
        Location = location;
        this.Id = id;
    }

    public PostModel() {
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        this.Cat = cat;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }
}
