package com.example.letslearn;

import java.util.Date;

public class Answer {
    private User user;
    private Date createDate;
    private String imageLink;
    private String id;

    public Answer(User user, Date createDate, String imageLink, String id) {
        this.user = user;
        this.createDate = createDate;
        this.imageLink = imageLink;
        this.id=id;
    }

    public Answer() {
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
