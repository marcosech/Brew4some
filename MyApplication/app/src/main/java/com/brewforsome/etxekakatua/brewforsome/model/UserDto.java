package com.brewforsome.etxekakatua.brewforsome.model;

/**
 * Created by etxekakatua on 1/12/17.
 */

public class UserDto {

    public String userName;
    public String userEmail;
    public String userPhone;
    public String id;

    public UserDto() {
    }

    public UserDto(String userName, String userEmail,String userPhone,String id) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
