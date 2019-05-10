package com.desarrolladorgmail.a.jose.stickerscity.POJOS;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Guardado {
    @PrimaryKey(autoGenerate =true)
    private long id;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "favorito")
    public boolean favorito;

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public Guardado(String url,boolean favorito) {
        this.url = url;
        this.favorito=favorito;
    }

    public String getUrl() {

        return url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
