package com.desarrolladorgmail.a.jose.stickerscity.Base_de_Datos;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Guardado;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;
//Clase donde creo la base de datos e indico de que clase va a hacer las tablas
@Database(entities = {Sticker.class,Guardado.class}, version = 1,exportSchema = false)
public  abstract class DataBaseRoom extends RoomDatabase {
    public abstract StickerDAO stickerDAO();
    private static DataBaseRoom INSTANCE=null;

    public static DataBaseRoom getInstance(final Context context){
        if(INSTANCE==null){
            INSTANCE=Room.databaseBuilder(context.getApplicationContext(),DataBaseRoom.class,"StickersFavorito.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
    public void destroyInstance(){INSTANCE=null;}

}
