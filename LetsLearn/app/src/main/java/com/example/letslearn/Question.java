package com.example.letslearn;

import java.util.Date;

public class Question {
    private String lesson;
    private User user;
    private Date createDate;
    private String imageLink;
    private String id;

    public Question(String lesson, User user, Date createDate, String imageLink, String id) {
        this.lesson = lesson;
        this.user = user;
        this.createDate = createDate;
        this.imageLink = imageLink;
        this.id=id;
    }

    public Question() {
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
