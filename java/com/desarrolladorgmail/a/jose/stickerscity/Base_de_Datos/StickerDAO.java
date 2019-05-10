package com.desarrolladorgmail.a.jose.stickerscity.Base_de_Datos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Guardado;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;

import java.util.List;
@Dao
public interface StickerDAO {
//Metodos encargados de gestionar datos en la bbdd
        @Insert
        long insert(Sticker s);

        @Insert
        long insert(Guardado g);

        @Delete
        void  delete(Sticker s);

        @Query("Select * from sticker;")
        LiveData<List<Sticker>> getProducts();

        @Query("Select * from sticker where id=(:id);")
        Sticker getProduct(String id);

        @Query("Delete FROM sticker;")
        void deleteFav();

        @Query("Delete FROM guardado;")
        void deleteGuardar();

        @Query("Select * from guardado order by id desc limit 6;")
        LiveData<List<Sticker>> getGifGuardado();
}
