package com.example.individualproject.models;

public class PostModel {
    private String type;
    private String title;
    private  String date;
    private String description;

    private String creator;

    public PostModel(String type, String title, String date, String description, String creator) {
        this.type = type;
        this.title = title;
        this.date = date;
        this.description = description;
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
