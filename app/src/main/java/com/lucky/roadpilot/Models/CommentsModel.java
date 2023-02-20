package com.lucky.roadpilot.Models;

public class CommentsModel {

    String name,pic,Category,time,Comment,uid;
    Long id;


    public CommentsModel(String name, String pic, String category, String time, String comment, String uid, Long id) {
        this.name = name;
        this.pic = pic;
        Category = category;
        this.time = time;
        Comment = comment;
        this.uid = uid;
        this.id = id;
    }

    public CommentsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

