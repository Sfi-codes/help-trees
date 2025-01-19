package com.example.individualproject.models;

public class UserModel {

    private String username;
    private Integer planted;

    private UserModel() {


    }
    public UserModel(String username, Integer planted) {
        this.username = username;
        this.planted = planted;
    }

    public UserModel(String username) {
        this.username = username;
        this.planted = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPlanted() {
        return planted;
    }

    public void setPlanted(Integer planted) {
        this.planted = planted;
    }

    public String toString(){
        return username + " planted " + planted;
    }

}
