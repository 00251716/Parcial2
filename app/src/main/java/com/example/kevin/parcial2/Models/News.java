package com.example.kevin.parcial2.Models;

//Esta clase sirve para almacenar la informaci�n de una noticia

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class News {

    @android.support.annotation.NonNull
    @PrimaryKey
    private String id = "default";

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="body")
    private String body;

    @ColumnInfo(name="game")
    private String game;

    @ColumnInfo(name="cover_image")
    private String coverImage;

    @ColumnInfo(name="created_date")
    private String created_date;

    @ColumnInfo(name="_V")
    private int _V;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int get_V() { return _V; }

    public void set_V(int v) { this._V = v; }
}
