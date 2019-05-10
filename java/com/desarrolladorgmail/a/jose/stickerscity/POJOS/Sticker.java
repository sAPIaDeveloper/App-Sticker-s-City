package com.desarrolladorgmail.a.jose.stickerscity.POJOS;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

@Entity
public class Sticker {
    @ColumnInfo(name = "url")
    public String url;
    @PrimaryKey @NonNull
    public String id;
    @ColumnInfo(name = "favorito")
    public boolean favorito;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public Sticker(String url, String id, boolean favorito) {

        this.url = url;
        this.id = id;
        this.favorito = favorito;
    }
}
