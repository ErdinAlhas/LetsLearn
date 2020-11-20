package com.example.letslearn;

import java.util.ArrayList;

public class User {
    private String UserName;
    private String EMail;
    private String Password;
    private String Key;

    public User(String userName, String email, String password){
        this.UserName=userName;
        this.EMail=email;
        this.Password=password;
    }
    public User(){
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String email) {
        this.EMail = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


}
