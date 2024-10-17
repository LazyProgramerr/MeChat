package com.sai.mechat.models;


import java.io.Serializable;

public class User implements Serializable {

    String userName,Uid,userMail,userPassword,userPhone,userPhoto,userFriends,userBio;

    public User() {
    }

    public User(String userName, String uid, String userMail, String userPassword, String userPhone, String userPhoto, String userFriends, String userBio) {
        this.userName = userName;
        Uid = uid;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userPhoto = userPhoto;
        this.userFriends = userFriends;
        this.userBio = userBio;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(String userFriends) {
        this.userFriends = userFriends;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }
}
