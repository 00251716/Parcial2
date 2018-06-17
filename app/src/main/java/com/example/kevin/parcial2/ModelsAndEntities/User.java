package com.example.kevin.parcial2.ModelsAndEntities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {

    @SerializedName("_id")
    private String id;
    private String user;
    private String password;
    private String[] favoriteNews;

    public User(String id, String user, String password) {
        this.id = id;
        this.user = user;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String[] getFavoriteNews() {
        return favoriteNews;
    }

}
